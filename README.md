# MyTVOnline3 Refresh Contents Viewer

An Android TV application that displays the latest contents sorted by ID queried from myTVOnline3 database.

<img width="1407" height="792" alt="image" src="https://github.com/user-attachments/assets/a8c714a7-1a35-4b18-8807-5e52df1b020d" />

## 📖 About

I was curious to see what contents were actually being pushed to my Formuler device as each time I selected "Refresh Contents" I expected to see updates in TV Series and VOD sections, but this didn't always happen.

The system logs contained more information that I could see via ADB commands locally, but they were difficult to access from a non-system application. Additionally, the logs were not persisted, making the available logcat output limited and unreliable at best.

As such, this app queries the Formuler content provider database, parses the results, and shows the latest content updates sorted by ID (newest first). When you refresh your content in MyTVOnline3, this viewer displays what content was added to the database (if any).


## 🚀 Installation

- Download the `.apk` from the [releases section](https://github.com/pxbt-dev/mytvOnline3-refresh-contents-viewer/releases) 
- Will only work on Formuler devices running mytvOnline3 (not tested on anything else!).
- Or compile from source (see below)
