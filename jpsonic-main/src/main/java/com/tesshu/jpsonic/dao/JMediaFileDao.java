/*
 This file is part of Jpsonic.

 Jpsonic is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Jpsonic is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Jpsonic.  If not, see <http://www.gnu.org/licenses/>.

 Copyright 2020 (C) tesshu.com
 */
package com.tesshu.jpsonic.dao;

import org.airsonic.player.dao.AbstractDao;
import org.airsonic.player.dao.MediaFileDao;
import org.airsonic.player.domain.Genre;
import org.airsonic.player.domain.MediaFile;
import org.airsonic.player.domain.MusicFolder;
import org.airsonic.player.domain.RandomSearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.BiFunction;

import static org.airsonic.player.dao.MediaFileDao.getGenreColoms;
import static org.airsonic.player.dao.MediaFileDao.getQueryColoms;
import static org.springframework.util.ObjectUtils.isEmpty;

@Repository("jmediaFileDao")
@DependsOn({ "mediaFileDao" })
public class JMediaFileDao extends AbstractDao {

    private static class AlbumSortCandidateMapper implements RowMapper<MediaFile> {
        public MediaFile mapRow(ResultSet rs, int rowNum) throws SQLException {
            MediaFile file = new MediaFile();
            file.setId(rs.getInt(1));
            file.setAlbumName(rs.getString(2));
            file.setAlbumReading(rs.getString(3));
            file.setAlbumSort(rs.getString(4));
            return file;
        }
    }

    private static class ArtistSortCandidateMapper implements RowMapper<MediaFile> {
        public MediaFile mapRow(ResultSet rs, int rowNum) throws SQLException {
            MediaFile file = new MediaFile();
            file.setId(rs.getInt(1));
            file.setArtist(rs.getString(2));
            file.setArtistReading(rs.getString(3));
            file.setArtistSort(rs.getString(4));
            return file;
        }
    }

    private static class MediaFileInternalRowMapper implements RowMapper<MediaFile> {

        private RowMapper<MediaFile> mediaFileMapper;

        public MediaFileInternalRowMapper(RowMapper<MediaFile> m) {
            super();
            this.mediaFileMapper = m;
        }

        @Override
        public MediaFile mapRow(ResultSet rs, int rowNum) throws SQLException {
            MediaFile mediaFile = mediaFileMapper.mapRow(rs, rowNum);
            mediaFile.setRownum(rs.getInt("irownum"));
            return mediaFile;
        }

    }

    private static final Logger LOG = LoggerFactory.getLogger(JMediaFileDao.class);

    private final MediaFileDao deligate;
    private final RowMapper<MediaFile> rowMapper;
    private final RowMapper<Genre> genreRowMapper;
    private final RowMapper<MediaFile> iRowMapper;
    private final RowMapper<MediaFile> artistSortCandidateMapper;
    private final RowMapper<MediaFile> albumSortCandidateMapper;

    public JMediaFileDao(MediaFileDao deligate) {
        super();
        this.deligate = deligate;
        rowMapper = deligate.getMediaFileMapper();
        genreRowMapper = deligate.getGenreMapper();
        iRowMapper = new MediaFileInternalRowMapper(rowMapper);
        artistSortCandidateMapper = new ArtistSortCandidateMapper();
        albumSortCandidateMapper = new AlbumSortCandidateMapper();
    }

    public void clearOrder() {
        update("update media_file set _order = -1");
    }

    public void createOrUpdateMediaFile(MediaFile file) {
        deligate.createOrUpdateMediaFile(file);
    }

    public List<MediaFile> getAlbumsByGenre(final int offset, final int count, final List<String> genres, final List<MusicFolder> musicFolders) {
        // @formatter:off
        if (musicFolders.isEmpty() || genres.isEmpty()) {
            return Collections.emptyList();
        }
        Map<String, Object> args = new HashMap<>();
        args.put("type", MediaFile.MediaType.ALBUM.name());
        args.put("genres", genres);
        args.put("folders", MusicFolder.toPathList(musicFolders));
        args.put("count", count);
        args.put("offset", offset);
        return namedQuery(
                "select " + getQueryColoms() + " from media_file " +
                "where type = :type and folder in (:folders) and present and genre in (:genres) " +
                "order by _order limit :count offset :offset", rowMapper, args);
    } // @formatter:on

    public List<MediaFile> getAlbumSortCandidate() { // @formatter:off
        return query(
                " select m.id as id, m.album as album, m.album_reading, dic.album_sort as album_sort"
                        + " from media_file m"
                        + " join (select distinct  album, album_sort from media_file where album_sort is not null and present order by album) dic"
                        + " on dic.album = m.album"
                        + " where type = ? and present"
                        + " and album_reading is not null"
                        + " and album_reading <> dic.album_sort order by album, album_sort",
                        albumSortCandidateMapper,
                        MediaFile.MediaType.ALBUM.name());
    } // @formatter:on

    public List<MediaFile> getAlphabeticalAlbums(final int offset, final int count, boolean byArtist, final List<MusicFolder> musicFolders) {
        return deligate.getAlphabeticalAlbums(offset, count, byArtist, musicFolders);
    }

    public List<MediaFile> getArtistAll(final List<MusicFolder> musicFolders) {
        if (musicFolders.isEmpty()) {
            return Collections.emptyList();
        }
        Map<String, Object> args = new HashMap<>();
        args.put("type", MediaFile.MediaType.DIRECTORY.name());
        args.put("folders", MusicFolder.toPathList(musicFolders));
        return namedQuery(// @formatter:off
                "select " + getQueryColoms() + " from media_file " +
                "where " +
                "type = :type and " +
                "folder in (:folders) and " +
                "present and " +
                "artist is not null", rowMapper, args); // @formatter:on
    }

    public List<MediaFile> getArtistSortCandidate() { // @formatter:off
        return query(
                "select m.id as id, m.artist as artist, artist_reading, dic.artist_sort as artist_sort from media_file m "
                        + "join (select distinct  artist, artist_sort from media_file where artist_sort is not null  and present order by artist) dic "
                        + "on dic.artist = m.artist "
                        + "where type = ? and present "
                        + "and artist_reading is not null "
                        + "order by artist, artist_sort",
                        artistSortCandidateMapper,
                        MediaFile.MediaType.DIRECTORY.name());
    } // @formatter:on

    /**
     * Returns the media file that are direct children of the given path.
     * 
     * @param path The path.
     * @return The list of children.
     */
    public List<MediaFile> getChildrenOf(final long offset, final long count, String path, boolean byYear) { // @formatter:off
        String order = byYear ? "year" : "_order";
        return query("select " + getQueryColoms() + " from media_file " +
                "where parent_path=? and present " +
                "order by " + order + " limit ? offset ?", rowMapper, path, count, offset);
    } // @formatter:on

    public int getChildSizeOf(String path) {
        return queryForInt("select count(id) from media_file where parent_path=? and present", 0, path);
    }

    public int getCountInPlaylist(int playlistId) { // @formatter:off
        return queryForInt("select count(*) from playlist_file, media_file " +
                "where media_file.id = playlist_file.media_file_id and playlist_file.playlist_id = ? and present ", 0, playlistId);
    } // @formatter:on

    public List<MediaFile> getFilesInPlaylist(int playlistId) {
        return deligate.getFilesInPlaylist(playlistId);

    }

    public List<MediaFile> getFilesInPlaylist(int playlistId, long offset, long count) { // @formatter:off
        return query("select " + prefix(getQueryColoms(), "media_file") + " from playlist_file, media_file " +
                     "where media_file.id = playlist_file.media_file_id and playlist_file.playlist_id = ? and present " +
                     "order by playlist_file.id limit ? offset ?", rowMapper, playlistId, count, offset);
    } // @formatter:on

    public List<Genre> getGenres(boolean sortByAlbum, long offset, long count) {
        String orderBy = sortByAlbum ? "album_count" : "song_count";
        return query("select " + getGenreColoms() + " from genre order by " + orderBy + " desc limit ? offset ?", genreRowMapper, count, offset);
    }

    public int getGenresCount() {
        return queryForInt("select count(*) from genre", 0);
    }

    public MediaFile getMediaFile(int id) {
        return deligate.getMediaFile(id);
    }

    public MediaFile getMediaFile(String path) {
        return deligate.getMediaFile(path);
    }

    public List<MediaFile> getRandomSongs(RandomSearchCriteria criteria, final String username) {
        if (criteria.getMusicFolders().isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, Object> args = new HashMap<>();
        args.put("folders", MusicFolder.toPathList(criteria.getMusicFolders()));
        args.put("username", username);
        args.put("fromYear", criteria.getFromYear());
        args.put("toYear", criteria.getToYear());
        args.put("genres", criteria.getGenres()); // TODO to be revert
        args.put("minLastPlayed", criteria.getMinLastPlayedDate());
        args.put("maxLastPlayed", criteria.getMaxLastPlayedDate());
        args.put("minAlbumRating", criteria.getMinAlbumRating());
        args.put("maxAlbumRating", criteria.getMaxAlbumRating());
        args.put("minPlayCount", criteria.getMinPlayCount());
        args.put("maxPlayCount", criteria.getMaxPlayCount());
        args.put("starred", criteria.isShowStarredSongs());
        args.put("unstarred", criteria.isShowUnstarredSongs());
        args.put("format", criteria.getFormat());

        boolean joinAlbumRating = (criteria.getMinAlbumRating() != null || criteria.getMaxAlbumRating() != null);
        boolean joinStarred = (criteria.isShowStarredSongs() ^ criteria.isShowUnstarredSongs());

        String query = "select " + prefix(getQueryColoms(), "media_file") + " from media_file ";

        if (joinStarred) {
            query += "left outer join starred_media_file on media_file.id = starred_media_file.media_file_id and starred_media_file.username = :username ";
        }

        if (joinAlbumRating) {
            query += "left outer join media_file media_album on media_album.type = 'ALBUM' and media_album.album = media_file.album and media_album.artist = media_file.artist ";
            query += "left outer join user_rating on user_rating.path = media_album.path and user_rating.username = :username ";
        }

        query += " where media_file.present and media_file.type = 'MUSIC'";

        if (!criteria.getMusicFolders().isEmpty()) {
            query += " and media_file.folder in (:folders)";
        }

        if (criteria.getGenres() != null) { // TODO to be revert
            query += " and media_file.genre in (:genres)";
        }

        if (criteria.getFormat() != null) {
            query += " and media_file.format = :format";
        }

        if (criteria.getFromYear() != null) {
            query += " and media_file.year >= :fromYear";
        }

        if (criteria.getToYear() != null) {
            query += " and media_file.year <= :toYear";
        }

        if (criteria.getMinLastPlayedDate() != null) {
            query += " and media_file.last_played >= :minLastPlayed";
        }

        if (criteria.getMaxLastPlayedDate() != null) {
            if (criteria.getMinLastPlayedDate() == null) {
                query += " and (media_file.last_played is null or media_file.last_played <= :maxLastPlayed)";
            } else {
                query += " and media_file.last_played <= :maxLastPlayed";
            }
        }

        if (criteria.getMinAlbumRating() != null) {
            query += " and user_rating.rating >= :minAlbumRating";
        }

        if (criteria.getMaxAlbumRating() != null) {
            if (criteria.getMinAlbumRating() == null) {
                query += " and (user_rating.rating is null or user_rating.rating <= :maxAlbumRating)";
            } else {
                query += " and user_rating.rating <= :maxAlbumRating";
            }
        }

        if (criteria.getMinPlayCount() != null) {
            query += " and media_file.play_count >= :minPlayCount";
        }

        if (criteria.getMaxPlayCount() != null) {
            if (criteria.getMinPlayCount() == null) {
                query += " and (media_file.play_count is null or media_file.play_count <= :maxPlayCount)";
            } else {
                query += " and media_file.play_count <= :maxPlayCount";
            }
        }

        if (criteria.isShowStarredSongs() && !criteria.isShowUnstarredSongs()) {
            query += " and starred_media_file.id is not null";
        }

        if (criteria.isShowUnstarredSongs() && !criteria.isShowStarredSongs()) {
            query += " and starred_media_file.id is null";
        }

        query += " order by rand()";

        query += " limit " + criteria.getCount();

        return namedQuery(query, rowMapper, args);
    }

    public List<MediaFile> getRandomSongsForAlbumArtist(// @formatter:off
            int limit, String albumArtist, List<MusicFolder> musicFolders,
            BiFunction< /** range */ Integer, /** limit */ Integer,
            List<Integer>> randomCallback) {

        String type = MediaFile.MediaType.MUSIC.name();

        /* Run the query twice. */

        /*
         * Get the number of records that match the conditions, to generate a set of
         * random numbers according to the number. Therefore, if the number of cases at
         * this time is too large, the subsequent performance is likely to be affected.
         * If the number isn't too large, it doesn't matter much.
         */
        int countAll = queryForInt("select count(*) from media_file where present and type = ? and album_artist = ?", 0, type, albumArtist);
        List<Integer> randomRownum = randomCallback.apply(countAll, limit);

        Map<String, Object> args = new HashMap<>();
        args.put("type", type);
        args.put("artist", albumArtist);
        args.put("randomRownum", randomRownum);
        args.put("limit", limit);

        /*
         * Perform a conditional search and add a row number.
         * Returns the result whose row number is included in the random number set.
         * 
         * There are some technical barriers to this query.
         * 
         *  (1) It must be a row number acquisition method that can be executed in all DBs.
         *  (2) It is simpler to join using UNNEST.
         *  However, hsqldb traditionally has a problem with UNNEST, and the operation specification differs depending on the version.
         *  In addition, compatibility of each DB may be affected.
         *  
         *  Therefore, we use a very primitive query that combines COUNT and IN here.
         *  
         *  IN allows you to get the smallest song subset corresponding to random numbers,
         *  but unlike JOIN&UNNEST, the order of random numbers is destroyed.
         */
        List<MediaFile> tmpResult = namedQuery(
                "select " + getQueryColoms() + ", foo.irownum from " +
                "    (select " +
                "        (select count(id) from media_file where id < boo.id and type = :type and album_artist = :artist) as irownum, * " +
                "    from (select * " +
                "        from media_file " +
                "        where type = :type " +
                "        and album_artist = :artist " +
                "        order by _order, album_artist, album) boo " +
                ") as foo " + 
                "where foo.irownum in ( :randomRownum ) limit :limit ", iRowMapper, args);

        /* Restore the order lost in IN. */
        Map<Integer, MediaFile> map = new HashMap<>();
        tmpResult.forEach(m -> map.put(m.getRownum(), m));
        List<MediaFile> result = new ArrayList<>();
        randomRownum.forEach(i -> {
            MediaFile m = map.get(i);
            if (!isEmpty(m)) {
                result.add(m);
            }
        });

        return Collections.unmodifiableList(result);
    } // @formatter:on

    public List<MediaFile> getSongsByGenre(final List<String> genres, final int offset, final int count, final List<MusicFolder> musicFolders) {
        if (musicFolders.isEmpty() || genres.isEmpty()) {
            return Collections.emptyList();
        }
        Map<String, Object> args = new HashMap<>();
        args.put("types", Arrays.asList(MediaFile.MediaType.MUSIC.name(), MediaFile.MediaType.PODCAST.name(), MediaFile.MediaType.AUDIOBOOK.name()));
        args.put("genres", genres);
        args.put("count", count);
        args.put("offset", offset);
        args.put("folders", MusicFolder.toPathList(musicFolders));
        // @formatter:off
        return namedQuery("select " + prefix(getQueryColoms(), "s") + " from media_file s " +
                          "join media_file al on s.parent_path = al.path " + 
                          "join media_file ar on al.parent_path = ar.path " +
                          "where s.type in (:types) and s.genre in (:genres) " +
                          "and s.present and s.folder in (:folders) " +
                          "order by ar._order, al._order, s.track_number " +
                          "limit :count offset :offset ", rowMapper, args);
    } // @formatter:on

    public int getSongsCountForAlbum(String artist, String album) { // @formatter:off
        return queryForInt(
                "select count(id) from media_file " +
                "where album_artist=? and album=? and present and type in (?,?,?)", 0,
                     artist, album, MediaFile.MediaType.MUSIC.name(), MediaFile.MediaType.AUDIOBOOK.name(), MediaFile.MediaType.PODCAST.name());
    } // @formatter:on

    public List<MediaFile> getSongsForAlbum(final long offset, final long count, MediaFile album) { // @formatter:off
        return query(
                "select " + getQueryColoms() + " from media_file " +
                "where parent_path=? and present and type in (?,?,?) order by track_number limit ? offset ?",
                rowMapper, album.getPath(), MediaFile.MediaType.MUSIC.name(), MediaFile.MediaType.AUDIOBOOK.name(),
                MediaFile.MediaType.PODCAST.name(), count, offset); // @formatter:on
    }

    public List<MediaFile> getSongsForAlbum(final long offset, final long count, String albumArtist, String album) { // @formatter:off
        return query(
                "select " + getQueryColoms() + " from media_file " +
                "where album_artist=? and album=? and present and type in (?,?,?) order by track_number limit ? offset ?",
                rowMapper, albumArtist, album, MediaFile.MediaType.MUSIC.name(), MediaFile.MediaType.AUDIOBOOK.name(),
                MediaFile.MediaType.PODCAST.name(), count, offset);
    } // @formatter:on

    public List<MediaFile> getSortedAlbums() { // @formatter:off
        return query("select " + getQueryColoms() +
                " from media_file" +
                " where album_reading is not null" +
                " or album_sort is not null" +
                " and type = ? and present",
                rowMapper, MediaFile.MediaType.ALBUM.name());
    } // @formatter:on

    public void markNonPresent(Date lastScanned) {
        deligate.markNonPresent(lastScanned);
    }

    public void markPresent(String path, Date lastScanned) {
        deligate.markPresent(path, lastScanned);
    }

    /**
     * Update albumArtistSorts all.
     * 
     * @param artist          The artist to update.
     * @param albumArtistSort Update value.
     */
    @Transactional
    public int updateAlbumArtistSort(String artist, String albumArtistSort) {
        LOG.trace("Updating media file at {}", artist);
        String sql = "update media_file set album_artist_sort = ? where artist = ? and artist_reading <> ? and type in (?, ?)";
        LOG.trace("Updating media file {}", artist);
        return update(sql, albumArtistSort, artist, albumArtistSort, MediaFile.MediaType.DIRECTORY.name(), MediaFile.MediaType.ALBUM.name());
    }

    /**
     * Update albumSorts all.
     * 
     * @param album     The artist to update.
     * @param albumSort Update value.
     */
    @Transactional
    public int updateAlbumSort(String album, String albumSort) {
        LOG.trace("Updating media file at {}", album);
        String sql = "update media_file set album_sort = ? where album = ? and type = ?";
        LOG.trace("Updating media file {}", album);
        return update(sql, albumSort, album, MediaFile.MediaType.ALBUM.name());
    }

    public void updateGenres(List<Genre> genres) {
        deligate.updateGenres(genres);
    }
}
