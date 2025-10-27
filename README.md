# MyTVOnline3 Refresh Contents Viewer

An Android TV application that displays the contents sorted by ID from myTVOnline3 db.

## 📖 About

I was curious to see what contents were actually being pushed as each time I selected "Refresh Contents" I was expecting to see updates in TV Series and VOD sections which didn't always happen.

The logs had more info as I could see from running ADB commands locally, though they were not persisted anywhere so were unreliable at best.

As such, this app queries the Formuler content provider database to show the latest content updates sorted by ID (newest first). When you refresh your content in MyTVOnline3, this viewer displays what content was added to the database (if any).

## 🚀 Installation

- Download the `.apk` from the [releases section](https://github.com/pxbt-dev/mytvOnline3-refresh-contents-viewer/releases) 
- Or compile from source (see below)
