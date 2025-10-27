package com.softsoftsoft.myonline3contentupdateviewer;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView logOutput;
    private ScrollView scrollView;
    private static final String TAG = "FormulerViewer";

    class Program {
        long id;
        String title;
        String description;
        String genre;
        int live;
        long startTime;
        long endTime;
        long duration;
        String seasonNumber;
        String episodeNumber;
        String episodeTitle;

        Program(long id, String title, String description, String genre, int live,
                long startTime, long endTime, long duration,
                String seasonNumber, String episodeNumber, String episodeTitle) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.genre = genre;
            this.live = live;
            this.startTime = startTime;
            this.endTime = endTime;
            this.duration = duration;
            this.seasonNumber = seasonNumber;
            this.episodeNumber = episodeNumber;
            this.episodeTitle = episodeTitle;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scrollView = new ScrollView(this);
        logOutput = new TextView(this);

        logOutput.setPadding(30, 30, 30, 30);
        logOutput.setTextSize(14);
        logOutput.setTextColor(0xFFFFFFFF);
        logOutput.setBackgroundColor(0xFF000000);

        scrollView.addView(logOutput);
        setContentView(scrollView);

        Log.i(TAG, "Formuler Content Viewer started");
        loadFormulerContent();
    }

    private void loadFormulerContent() {
        try {
            StringBuilder content = new StringBuilder();
            content.append("Please Refresh Contents in MyOnlineTV3 to see newer content\n");
            content.append("====================================================\n\n");

            Cursor cursor = getContentResolver().query(
                    Uri.parse("content://formuler.media.tv/preview_program"),
                    new String[]{
                            "_id", "title", "short_description", "long_description", "genre",
                            "live", "start_time_utc_millis", "end_time_utc_millis",
                            "duration_millis", "season_display_number",
                            "episode_display_number", "episode_title"
                    },
                    null, null, null
            );

            if (cursor == null) {
                content.append("❌ ERROR: Cursor is null\n");
                logOutput.setText(content.toString());
                return;
            }

            List<Program> programs = new ArrayList<>();
            int count = 0;

            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex("_id");
                int titleIndex = cursor.getColumnIndex("title");
                int shortDescIndex = cursor.getColumnIndex("short_description");
                int longDescIndex = cursor.getColumnIndex("long_description");
                int genreIndex = cursor.getColumnIndex("genre");
                int liveIndex = cursor.getColumnIndex("live");
                int startTimeIndex = cursor.getColumnIndex("start_time_utc_millis");
                int endTimeIndex = cursor.getColumnIndex("end_time_utc_millis");
                int durationIndex = cursor.getColumnIndex("duration_millis");
                int seasonNumberIndex = cursor.getColumnIndex("season_display_number");
                int episodeNumberIndex = cursor.getColumnIndex("episode_display_number");
                int episodeTitleIndex = cursor.getColumnIndex("episode_title");

                do {
                    long id = idIndex != -1 ? cursor.getLong(idIndex) : -1;
                    String title = titleIndex != -1 ? cursor.getString(titleIndex) : null;
                    String shortDesc = shortDescIndex != -1 ? cursor.getString(shortDescIndex) : "";
                    String longDesc = longDescIndex != -1 ? cursor.getString(longDescIndex) : "";
                    String genre = genreIndex != -1 ? cursor.getString(genreIndex) : null;
                    int live = liveIndex != -1 ? cursor.getInt(liveIndex) : 0;
                    long startTime = startTimeIndex != -1 ? cursor.getLong(startTimeIndex) : 0;
                    long endTime = endTimeIndex != -1 ? cursor.getLong(endTimeIndex) : 0;
                    long duration = durationIndex != -1 ? cursor.getLong(durationIndex) : 0;
                    String seasonNumber = seasonNumberIndex != -1 ? cursor.getString(seasonNumberIndex) : null;
                    String episodeNumber = episodeNumberIndex != -1 ? cursor.getString(episodeNumberIndex) : null;
                    String episodeTitle = episodeTitleIndex != -1 ? cursor.getString(episodeTitleIndex) : null;

                    String description = longDesc != null && !longDesc.isEmpty() ? longDesc : shortDesc;

                    if (description != null && !description.trim().isEmpty()) {
                        programs.add(new Program(id, title, description, genre, live,
                                startTime, endTime, duration,
                                seasonNumber, episodeNumber, episodeTitle));
                        count++;
                    }

                } while (cursor.moveToNext());

                cursor.close();

                // Sort by ID (newest first)
                Collections.sort(programs, new Comparator<Program>() {
                    @Override
                    public int compare(Program p1, Program p2) {
                        return Long.compare(p2.id, p1.id);
                    }
                });

                // Clean display - NO LIMIT!
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm", Locale.getDefault());

                for (Program program : programs) {
                    content.append("🆔 ID: ").append(program.id).append("\n");

                    // Title
                    if (program.title != null) {
                        content.append("📺 ").append(program.title).append("\n");
                    }

                    // Genre (if available)
                    if (program.genre != null) {
                        content.append("🎭 ").append(program.genre).append("\n");
                    }

                    // Description
                    content.append("📝 ").append(program.description).append("\n");

                    // Series info (if available)
                    if (program.seasonNumber != null || program.episodeNumber != null) {
                        content.append("🎬 ");
                        if (program.seasonNumber != null) content.append("S").append(program.seasonNumber);
                        if (program.episodeNumber != null) content.append("E").append(program.episodeNumber);
                        if (program.episodeTitle != null) content.append(" - ").append(program.episodeTitle);
                        content.append("\n");
                    }

                    // Timing information
                    if (program.startTime > 0) {
                        Date startDate = new Date(program.startTime);
                        content.append("🕒 ").append(sdf.format(startDate));

                        if (program.endTime > program.startTime) {
                            Date endDate = new Date(program.endTime);
                            long durationMinutes = (program.endTime - program.startTime) / (1000 * 60);
                            content.append(" (").append(durationMinutes).append("min)");
                        }
                        content.append("\n");
                    } else if (program.duration > 0) {
                        long durationMinutes = program.duration / (1000 * 60);
                        content.append("⏱️ ").append(durationMinutes).append(" min\n");
                    }

                    // Live indicator
                    if (program.live == 1) {
                        content.append("🔴 LIVE\n");
                    }

                    content.append("────────────────────────\n\n");
                }

                content.append("\n📊 Total: ").append(programs.size()).append(" items");

            } else {
                content.append("ℹ️ No programs found\n");
            }

            logOutput.setText(content.toString());

        } catch (SecurityException e) {
            Log.e(TAG, "Permission denied: " + e.getMessage());
            logOutput.setText("❌ PERMISSION DENIED\n\nCannot access Formuler database.");
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
            logOutput.setText("❌ ERROR\n\n" + e.getMessage());
        }
    }
}