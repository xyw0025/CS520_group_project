/**
 * @swagger
 * /api/users/{user_id}:
 *   get:
 *     description: return user's record
 *     parameters:
 *        - in: path
 *          name: userId
 *          schema:
 *          type: integer
 *          required: true
 *     responses:
 *       200:
 *         description: {}
 */

export async function GET(_request: Request) {
    // Do whatever you want
    return new Response('Hello World!', {
      status: 200,
    });
  }


/**
 * @swagger
 * /api/users:
 *   post:
 *     summary: Create a new user
 *     description: Endpoint to create a new user.
 *     responses:
 *       '201':
 *         description: User created successfully
 *       '400':
 *         description: Bad request
 *     parameters:
 *       - in: query
 *         name: name
 *         required: true
 *         type: string
 *         description: The name of the user.
 *       - in: query
 *         name: email
 *         required: true
 *         type: string
 *         description: The email address of the user.
 *       - in: query
 *         name: password
 *         required: true
 *         type: string
 *         description: The user's password.
 */
