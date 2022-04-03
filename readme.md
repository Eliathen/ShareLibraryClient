# Sharelibrary client - android client for book exchange application
> Application build as graduate work for bachelor's degree.

## General Information
The main reason to build application was to let users find people from neighborhood and exchange books with them either temporary or permament. User set his exchange place using latitude and longitude. Others can find this location and see what books this user want to borrow or look for. Then they can create a request for exchange. Owner can see for what book he can exchange and then agree or reject. Later they can communicate via chat to set details like date. 

Backend for application: https://github.com/Eliathen/sharelibrary

## Technologies Used
- Kotlin 1.4.1
- Retrofit2
- Koin
- Osmdroid


## Features
- Create exchange
- Filter exchanges by GPS or distance
- Filter exchanges by books details: language, condition, category
- Users can communicate with others via chat

# Screenshots
| ![](./screenshots/adding_book.png)        | ![](./screenshots/exchanges_view_map.png) | ![](./screenshots/filtering.png)          |
|                  :----:                   |                  :----:                   |                  :----:                   |
| Adding book for exchange. All fields are mandatory| Displaying current exchanges using map.<br>Orange icon stand for user's current exchange place,<br>red is others' exchanges and black is user's current location| Beside standard book's details like<br>language and category users can filter<br>by distance from current location.                             |
|                  :----                    |                  :----                    |                  :----                    |


