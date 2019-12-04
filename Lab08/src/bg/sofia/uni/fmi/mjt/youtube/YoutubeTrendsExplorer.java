package bg.sofia.uni.fmi.mjt.youtube;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import bg.sofia.uni.fmi.mjt.youtube.model.TrendingVideo;

public class YoutubeTrendsExplorer {
    private Map<String, List<TrendingVideo>> trendingVideos;

    /**
     * Loads the dataset from the given {@code dataInput} stream.
     */
    public YoutubeTrendsExplorer(InputStream dataInput) {
        trendingVideos = new HashMap<>();
        loadTrendingVideos(dataInput);
    }

    /**
     * Returns all videos loaded from the dataset.
     **/
    public Collection<TrendingVideo> getTrendingVideos() {
        return trendingVideos.values()
                .stream()
                .flatMap(list -> list.stream())
                .collect(Collectors.toList());
    }

    /**
     * Returns the ID of the least liked trending video.
     */
    public String findIdOfLeastLikedVideo() {
        return getTrendingVideos().stream()
                .min(Comparator.comparing(TrendingVideo::getLikes))
                .get()
                .getId();
    }

    /**
     * Return the ID of the most likeable video.
     */
    public String findIdOfMostLikedLeastDislikedVideo() {
        return getTrendingVideos().stream()
                .max(Comparator.comparing(TrendingVideo::getLikeRatio))
                .get()
                .getId();
    }

    /**
     * Returns list of titles of the most watched trending videos.
     */
    public List<String> findDistinctTitlesOfTop3VideosByViews() {
        final int total = 3;
        Comparator<TrendingVideo> compareByViews = Comparator.comparing(TrendingVideo::getViews);
        
        return trendingVideos.values()
                .stream()
                .map(list -> list.stream().max(compareByViews).get())
                .sorted(compareByViews.reversed())
                .limit(total)
                .map(TrendingVideo::getTitle)
                .collect(Collectors.toList());
    }

    /**
     * Returns the id of the most tagged video.
     */
    public String findIdOfMostTaggedVideo() {
        return getTrendingVideos().stream()
                .max((a, b) -> Integer.compare(a.getTags().size(), b.getTags().size()))
                .get()
                .getId();
    }

    /**
     * Returns the first trending video before it got 100K views
     */
    public String findTitleOfFirstVideoTrendingBefore100KViews() {
        final int maxViews = 100_000;
        
        return getTrendingVideos().stream()
                .filter(video -> video.getViews() < maxViews)
                .min(Comparator.comparing(TrendingVideo::getPublishDate))
                .get()
                .getTitle();
    }

    /**
     * Returns the id of the most trending video
     */
    public String findIdOfMostTrendingVideo() {
        return trendingVideos.values()
                .stream()
                .max(Comparator.comparing(List::size))
                .get()
                .get(0)
                .getId();
    }

    private void loadTrendingVideos(InputStream dataInput) {
        try (var reader = new BufferedReader(new InputStreamReader(dataInput))) {
            reader.readLine();
            String line = reader.readLine();

            while (line != null) {
                addTrendingVideo(TrendingVideo.createTrendingVideo(line));
                line = reader.readLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void addTrendingVideo(TrendingVideo video) {
        String videoId = video.getId();

        if (!trendingVideos.containsKey(videoId)) {
            trendingVideos.put(videoId, new ArrayList<>());
        }
        
        trendingVideos.get(videoId).add(video);
    }

    
}