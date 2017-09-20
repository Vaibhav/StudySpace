# StudySpace

https://devpost.com/software/study-space

## What it does

Study Space is an app that keeps track of the number of people in specific locations on campus in real time. It lets the user of the app figure out which rooms in campus are the least busy thus allowing for easier access to quiet study spots.

## How we built it

To build this app we used Android Studio to create client facing android apps for users phones as well as an app to be displayed on AndroidThings screens. We also used the 'Android Nearby' feature that is a part of AndroidThings to sniff the number of wireless devices in an area, and Firebase to store the number of devices, thus determining the occupancy, within an area.

## Challenges we ran into

We ran into many issues with AndroidThings not connecting to the internet (after 8 hours we realized it was a simple configuration issue where the internet connection in the AndroidManifest.xml was set to 'no'). We also had trouble figuring out the best way to sniff out devices with WiFi connectivity in a certain area since there are privacy concerns associated with getting this kind of data. In the end, we decided that ideally this app would be incorporated within existing University affiliated apps (e.g. PennMobile App) where the user would need to accept a condition stating that the app will anonymously log the phone's location strictly for this purpose.

## What we learned

We learned that sometimes working with hardware can be a pain in the butt. However, in the end, we found this hack to be very rewarding as it allowed us to create an end product that is only able to function due to the capabilities of the hardware included in AndroidThings. We also learned how to make native android apps (it was the first time that 2 members of our group ever created an android app with native code).


## What's next for Study Space

In the future, we would like to incorporate trends into our app in order to show users charts about when study areas are at their maximum/minimum occupancy. This would allow users to better plan future study session accordingly. We would also like to include push notifications with the app so that users are informed, at a time of their choosing, of the least busy places to study on campus.
