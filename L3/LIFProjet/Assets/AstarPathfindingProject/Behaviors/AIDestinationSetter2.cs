using UnityEngine;
using System.Collections;
using UnityEngine.UI;

namespace Pathfinding {
	/// <summary>
	/// Sets the destination of an AI to the position of a specified object.
	/// This component should be attached to a GameObject together with a movement script such as AIPath, RichAI or AILerp.
	/// This component will then make the AI move towards the <see cref="target"/> set on this component.
	///
	/// See: <see cref="Pathfinding.IAstarAI.destination"/>
	///
	/// [Open online documentation to see images]
	/// </summary>
	[UniqueComponent(tag = "ai.destination")]
	[HelpURL("http://arongranberg.com/astar/docs/class_pathfinding_1_1_a_i_destination_setter.php")]
	public class AIDestinationSetter2 : VersionedMonoBehaviour {
		/// <summary>The object that the AI should move to</summary>
		IAstarAI ai;
		public GameObject targetA;
		public GameObject targetB;

		Transform target;
		Transform target2;
		Transform targetFinal;

		int vision = 7;

		Animator anim;

		public float updateInterval = 3f;
		public double lastInterval;

		void OnEnable () {
			ai = GetComponent<IAstarAI>();
			anim = GetComponent<Animator>();

			target = targetA.transform;
			target2 = targetB.transform;

			
			// Update the destination right before searching for a path as well.
			// This is enough in theory, but this script will also update the destination every
			// frame as the destination is used for debugging and may be used for other things by other
			// scripts as well. So it makes sense that it is up to date every frame.
			if (ai != null) ai.onSearchPath += Update;
		}

		void OnDisable () {
			if (ai != null) ai.onSearchPath -= Update;
		}

		/// <summary>Updates the AI's destination every frame</summary>
		void Update () {


			//Calcul de la distance entre le gameObject et les ennemis
			float distance;
			float distance2;

			if(targetA.activeSelf)
			{
				distance = Mathf.Sqrt( Mathf.Pow(gameObject.transform.position.x - target.position.x, 2) + Mathf.Pow(gameObject.transform.position.y - target.position.y, 2)  );
			} else distance = 1000;

			if(targetB.activeSelf)
			{
				distance2 = Mathf.Sqrt( Mathf.Pow(gameObject.transform.position.x - target2.position.x, 2) + Mathf.Pow(gameObject.transform.position.y - target2.position.y, 2)  );
			} else distance2 = 1000;

			//récupération des pv ennemis
			//int targetPV = targetA.GetComponent<Data>().currentHealth;
			//int targetPV2 = targetB.GetComponent<Data>().currentHealth;

			
			//choix de la cible finale
			//if(targetPV < targetPV2) targetFinal = target;
			
			//if(targetPV > targetPV2) targetFinal = target2;
			
			//if(targetPV == targetPV2)
			//{
				if(distance<distance2)
				{
					targetFinal = target;
				} else {
					targetFinal = target2;
				}
			//}

			
			
			

			// s'il ne voit pas d'ennemi dans son champ de vision, il se déplace random
			if (distance > vision && distance2 > vision)
			{
				float timeNow = Time.realtimeSinceStartup;

				if( timeNow - lastInterval > updateInterval)
				{
					float x = Random.Range(-11,10);
					float y = Random.Range(-6,8);
					Vector2 pos = new Vector2(x, y);
				
					ai.destination = pos;

					lastInterval = timeNow;
				}
				
			} else { //sinon il attaque l'ennemi à portée de vue
				if (target != null && ai != null) 
				{
					ai.destination = targetFinal.position;
				}
			}
		
			SetParam();
		
		}

		

		void SetParam()
		{
			if(ai.destination.x > 0  && Mathf.Abs(ai.destination.x - gameObject.transform.position.x) > Mathf.Abs(ai.destination.y - gameObject.transform.position.y))
			{
				anim.SetInteger("direction",1);
			}
			else if(ai.destination.x < 0 && Mathf.Abs(ai.destination.x - gameObject.transform.position.x) > Mathf.Abs(ai.destination.y - gameObject.transform.position.y))
			{
				anim.SetInteger("direction",2);
			}
			else if(ai.destination.y > 0 && Mathf.Abs(ai.destination.x - gameObject.transform.position.x) < Mathf.Abs(ai.destination.y - gameObject.transform.position.y))
			{
				anim.SetInteger("direction",3);
			}
			else if(ai.destination.y < 0 && Mathf.Abs(ai.destination.x - gameObject.transform.position.x) < Mathf.Abs(ai.destination.y - gameObject.transform.position.y))
			{
				anim.SetInteger("direction",4);
			}
			else if(ai.destination.x == 0 && ai.destination.y == 0)
			{
				anim.SetInteger("direction",0);
			}
			
		}

	}
}
