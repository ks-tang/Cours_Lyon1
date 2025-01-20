
using UnityEngine;

public class MovePerso : MonoBehaviour
{
    public float moveSpeed;
	
	public Rigidbody2D rb;
	
	private Vector3 velocity = Vector3.zero;

    // Update is called once per frame
    void FixedUpdate()
    {
        float horizontalMovement = Input.GetAxis("Horizontal") * moveSpeed * Time.deltaTime;
		
		float verticalMovement = Input.GetAxis("Vertical") * moveSpeed * Time.deltaTime;
		
		MovePlayer(horizontalMovement);
		
		MovePlayerV(verticalMovement);

	}
	
	void MovePlayer(float _horizontalMovement)
	{
		Vector3 targetVelocity = new Vector2(_horizontalMovement, rb.velocity.y);
		rb.velocity = Vector3.SmoothDamp(rb.velocity, targetVelocity, ref velocity, 0.05f);
	}
	
	void MovePlayerV(float _verticalMovement)
	{
		Vector3 targetVelocity = new Vector2(rb.velocity.x, _verticalMovement);
		rb.velocity = Vector3.SmoothDamp(rb.velocity, targetVelocity, ref velocity, 0.05f);
	}
}
