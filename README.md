# PostService

This project is my future diploma work, now it is in the process of development.
In it I use:
- Java Core
- Spring: Security, Boot, Data, MVC, REST
- HIbernate
- PostgreSQL
- Server Tomcat
- Thymeleaf, Bootstrap, HTML, CSS, JS

Brief description:
A site for controlling the logic of the interaction between the sender and the executor, specializing in large cargo shipments.

There are three roles for interacting with the web application:
- Admin
- Performer
- Employer

The role is selected during registration. Performer, when registered, must submit his profile for validation:

![profile](https://user-images.githubusercontent.com/62800741/225065425-1c028065-e76e-4e5c-aab6-83bdc44bc468.PNG)

Validation is performed by the administrator after checking the specified data:

![image](https://user-images.githubusercontent.com/62800741/225066223-e1b1aae4-cdca-40f2-9435-aa139004064a.png)

After validation, the Performer can select an order to execute. A Performer is a person who has a suitable vehicle for transportation and is ready to do the job:

![shipment_list](https://user-images.githubusercontent.com/62800741/225066751-adf15da4-574a-4ba5-be98-f2c0e40c096e.PNG)

The Performer changes the status of the order during the execution of the work.
There are three order statuses, simply put:
- the order was taken, it has not started to be fulfilled
- the order has begun to be fulfilled
- completed order

Employer has a page for placing an order and a list of created orders:
![image](https://user-images.githubusercontent.com/62800741/225068524-aab001e9-7c23-4987-8580-1b54cc40a13a.png)

![image](https://user-images.githubusercontent.com/62800741/225068596-4686233e-9c8e-441b-8645-8d85dcf0481c.png)

You can also view each order:
![shipment_information](https://user-images.githubusercontent.com/62800741/225068306-a16f52da-46d6-4445-8e75-c51053355e4a.PNG)

According to the status of the order, the list of Actions that can be performed with it changes

REST API:
![rest1](https://user-images.githubusercontent.com/62800741/226137039-58d2079a-d1f0-4231-ab45-1cca124a8b52.PNG)
![rest2](https://user-images.githubusercontent.com/62800741/226137044-eaa5d201-5dfe-4e1d-9ef9-dd8e1335a09d.PNG)


