# MarketPal Backend
Get a short overview of codebase to understand what actually it does 

# What This Backend Does??
1.MarketPal backend is a REST API built with Spring Boot.

2.It handles everything the React frontend needs — user registration with email verification, secure login using JWT tokens, product listing with permanent image storage on Cloudinary, and ownership-based protection so only the person who listed a product can edit or delete it.

3.It stores permanent data in MySQL (TiDB Cloud), temporary data in Redis (Upstash), sends emails via Resend, and stores images on Cloudinary.

4.Every endpoint either requires a JWT token or is explicitly made public.

5.The single most important improvement over the old MarketPal: everything is enforced at the backend level.

6.A buyer cannot edit a seller's product by faking a request.

7.A user cannot skip email verification by modifying localStorage. 

8.The backend validates everything independently of what the frontend says.
