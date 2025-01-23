package stormTP.topology;

import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import stormTP.operator.Exit2Bolt;
import stormTP.operator.MyTortoiseBolt;
import stormTP.operator.MasterInputStreamSpout;
import stormTP.operator.NothingBolt;

/**
 *
 * @author lumineau
 * Topologie test permettant d'écouter le Master Input
 *
 */
public class TopologyT2 {

	public static void main(String[] args) throws Exception {
		int nbExecutors = 1;
		int room = Integer.parseInt(args[0]);
		int portINPUT = 9000 + room;
		int portOUTPUT = 9005;
		String ipmINPUT = "224.0.0." + room;
		String ipmOUTPUT = "225.0.0." + room ;

		/*Création du spout*/
    	MasterInputStreamSpout spout = new MasterInputStreamSpout(portINPUT, ipmINPUT);
    	/*Création de la topologie*/
    	TopologyBuilder builder = new TopologyBuilder();
        /*Affectation à la topologie du spout*/
        builder.setSpout("masterStream", spout);
        /*Affectation à la topologie du bolt qui ne fait rien, il prendra en input le spout localStream*/
        builder.setBolt("nofilter", new NothingBolt(), nbExecutors).shuffleGrouping("masterStream");

        String binomeName = "Nguyen-Tang";
        builder.setBolt("myTortoiseBolt", new MyTortoiseBolt(), nbExecutors).shuffleGrouping("masterStream");
        builder.setBolt("exit2", new Exit2Bolt(portOUTPUT, ipmOUTPUT), nbExecutors).shuffleGrouping("myTortoiseBolt");

        /*Création d'une configuration*/
        Config config = new Config();
        /*La topologie est soumise à STORM*/
        StormSubmitter.submitTopology("topoT2", config, builder.createTopology());
	}


}