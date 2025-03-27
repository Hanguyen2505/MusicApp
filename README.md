There are a vast number of music app available nowaadays such as Sportify, Music App,... THey offer a enjoyable experience to the users. I, as a rookie in Mobile Development, want to create an engaing app with a music playback feature as well.
This app provides access to a wide music library, allow users to listen to music without the need of downloading the files. 

This app took me about 2 months of development. To be specific, the project started on 3rd September 2024 and was nearly completed by 20th November 2024.
I was solely responsible for building this app. The development process involved:
1. Planning and Research (1 week)
2. Design UI, UX, Database (5 days)
3. Development (7 weeks)
4. Testing(1 week)
5. Deploy and maintenance(not yet completed)

Some techniques I used during the development process:
Language: Kotlin
Tool: Figma
Architecture Pa{ern: MVVM
Backend Platform: Firebase
Library: Exoplayer, Glide
Others: Dagger-Hilt, Coroutines, Navigation Component, LiveData...

**Here are some key features:**
1. Access to a large music library(Admin can also add or delete audio contents when he/she wants).
![image](https://github.com/user-attachments/assets/edddbb6f-864c-417d-883f-5679e6065dd2)

2. Playing music in the backgorund and displaying playback notification so users can listen to music even after exiting the app.
![image](https://github.com/user-attachments/assets/1394f213-125e-46fc-959a-9bf3d79e8240)

3. Classifying songs into different genres, artists, playlists and ability to search for artist and songs.
![image](https://github.com/user-attachments/assets/98297922-d655-478d-a78c-eee499b07317) ![image](https://github.com/user-attachments/assets/fd9ddbcf-34d5-43e9-813a-8fde0d30284b) ![image](https://github.com/user-attachments/assets/a3d78e45-312b-4472-8a61-d3825b6b7601) ![image](https://github.com/user-attachments/assets/0e9be7a2-57f7-4af2-ab62-e751a8e4bdbb)

4. Custom control for media player like play, pause, rewind, skip to the next song...                                     
![{524914EB-B5BA-464E-95D2-627D960CC6E8}](https://github.com/user-attachments/assets/fcb0179a-297f-4430-b7a2-04ecbaa9a3fc)

5. Authentication via Email or google Sign-In.                                          
![image](https://github.com/user-attachments/assets/60a3c257-b195-4be8-a3c3-575e95699128)
![{2304A3A4-74C5-4580-8340-E5D0ED35D526}](https://github.com/user-attachments/assets/28f8262c-a19c-4b46-a64d-9cc050417142)

6. Personalize playlist.                                                                     
![{5364FE30-48FA-4C9B-A37D-7AAA2BEB1912}](https://github.com/user-attachments/assets/fe1f8b0c-836c-4046-a716-15cfc9f93a6f)        ![{B616A415-D2B3-41AB-941F-2FAF81592D8F}](https://github.com/user-attachments/assets/a45bc1f0-bfa1-4a8b-9922-891662b6cac**8)

**Detail in work flow: **
1. Remote database:
  This app stores images, MP3 file in firebase storage. It generated a url link for each file then store it along with the users, artists, songs, playlists,... information in the firebase firestore.

2. Background service:
  The song data collected from remote database will be converted into Media Metadata by Firebase Source.
  Music service will load media items into Exoplayer, enable media song control.
  Media Factory will convert meida metadata back into readable data to display on screen.
  
3. Viemwmodel:
  Viewmodel will collect data from Media Factory and return the results as livedata.
  With this approach, the applicaton is guaranteed to follow MVVM and Observer Pattern, the Viewmodel will not need to inform the change to the UI, the UI will be able to observe and if any changes happen, It will update itselft.

**Here is the process flow diagram:**                                                                                  
![{14F1E32B-DBF3-419F-8F29-A75A103CEEF0}](https://github.com/user-attachments/assets/1e540b6b-81e2-4824-8fa2-64f4cda80427)


I have learnt a lot during the app development. Some of those skills are:
1. Handling media playback using Exoplayer.
2. Basic knowledge of Dependency Injection, managing dependencies efficiently to avoid boilerplate code.
3. Comprehend the basics of Coroutines for efficient background processing and prevent UI freeze.
4. Approaching the definition of Observe Pattern and how It works with MVVM.
5. A deeper understanding of Firebase
