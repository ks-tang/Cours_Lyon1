using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Weakspot : MonoBehaviour
{
    public GameObject ennemi;
    public void CollisionEnnemi(GameObject ennemi)
    {

        ennemi.GetComponent<Wolf>().TakeDamage(20); //fais 20 dégats


    }
}
