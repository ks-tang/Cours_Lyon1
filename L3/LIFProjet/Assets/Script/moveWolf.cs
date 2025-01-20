using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class moveWolf : MonoBehaviour
{
    public float moveSpeed;
    Vector3 target;
    public float SpeedOfChangingTarget = 5;
    private float camHeight;
    private float camWidth;
    private float circleRadious = 2;
    SpriteRenderer spriteRenderer;
    void Start()
    {
        Camera cam = Camera.main;
        camHeight = 2f * cam.orthographicSize;
        camWidth = camHeight * cam.aspect;

        spriteRenderer = GetComponent<SpriteRenderer>();

        InvokeRepeating("GenerateNewTarget", 0f, SpeedOfChangingTarget);
    }

    void Update()
    {
        circleRadious = spriteRenderer.bounds.size.x / 2;
        Vector3 currentPos = transform.position;
        if (currentPos == target)
        {
            GenerateNewTarget();
        }
        transform.position = Vector3.MoveTowards(currentPos, target, Time.deltaTime * moveSpeed);//movement from current position to target position
    }
    void OnCollisionStay2D()
    {
        GenerateNewTarget();
    }
    void GenerateNewTarget()
    {
        target = new Vector3(Random.Range(-(camWidth / 2 - circleRadious), camWidth / 2 - circleRadious), Random.Range(-(camHeight / 2 - circleRadious), camHeight / 2 - circleRadious), 0); //again provide random position in x and y

    }
}
