/*
 This file is part of Airsonic.

 Airsonic is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Airsonic is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Airsonic.  If not, see <http://www.gnu.org/licenses/>.

 Copyright 2016 (C) Airsonic Authors
 Based upon Subsonic, Copyright 2009 (C) Sindre Mehus
 */
package org.airsonic.player.domain;

import com.tesshu.jpsonic.domain.FontScheme;

import java.util.Date;
import java.util.Locale;

/**
 * Represent user-specific settings.
 *
 * @author Sindre Mehus
 */
public class UserSettings {

    private String username;
    private Locale locale;
    private String themeId;
    private boolean showNowPlayingEnabled;
    private boolean showArtistInfoEnabled;
    private boolean finalVersionNotificationEnabled;
    private boolean betaVersionNotificationEnabled;
    private boolean songNotificationEnabled;
    private boolean keyboardShortcutsEnabled;
    private boolean autoHidePlayQueue;
    private boolean viewAsList;
    private boolean queueFollowingSongs;
    private AlbumListType defaultAlbumList = AlbumListType.RANDOM;
    private Visibility mainVisibility = new Visibility();
    private Visibility playlistVisibility = new Visibility();
    private boolean lastFmEnabled;
    private boolean listenBrainzEnabled;
    private String lastFmUsername;
    private String lastFmPassword;
    private String listenBrainzToken;
    private TranscodeScheme transcodeScheme = TranscodeScheme.OFF;
    private int selectedMusicFolderId = -1;
    private boolean partyModeEnabled;
    private boolean nowPlayingAllowed;
    private AvatarScheme avatarScheme = AvatarScheme.NONE;
    private Integer systemAvatarId;
    private Date changed = new Date();
    private int paginationSize;

    // JP >>>>
    private boolean closeDrawer;
    private boolean closePlayQueue;
    private boolean alternativeDrawer;
    private boolean showIndex;
    private boolean assignAccesskeyToNumber;
    private boolean openDetailIndex;
    private boolean openDetailSetting;
    private boolean openDetailStar;
    private boolean simpleDisplay;
    private boolean showSibling;
    private boolean showRate;
    private boolean showAlbumSearch;
    private boolean showLastPlay;
    private boolean showDownload;
    private boolean showTag;
    private boolean showComment;
    private boolean showShare;
    private boolean showChangeCoverArt;
    private boolean showTopSongs;
    private boolean showSimilar;
    private boolean showAlbumActions;
    private boolean breadcrumbIndex;
    private boolean putMenuInDrawer;
    private String fontSchemeName = FontScheme.DEFAULT.name();
    private boolean showOutlineHelp;
    private boolean forceBio2Eng;
    // <<<< JP

    public UserSettings(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getThemeId() {
        return themeId;
    }

    public void setThemeId(String themeId) {
        this.themeId = themeId;
    }

    public boolean isShowNowPlayingEnabled() {
        return showNowPlayingEnabled;
    }

    public void setShowNowPlayingEnabled(boolean showNowPlayingEnabled) {
        this.showNowPlayingEnabled = showNowPlayingEnabled;
    }

    public boolean isShowArtistInfoEnabled() {
        return showArtistInfoEnabled;
    }

    public void setShowArtistInfoEnabled(boolean showArtistInfoEnabled) {
        this.showArtistInfoEnabled = showArtistInfoEnabled;
    }

    public boolean isFinalVersionNotificationEnabled() {
        return finalVersionNotificationEnabled;
    }

    public void setFinalVersionNotificationEnabled(boolean finalVersionNotificationEnabled) {
        this.finalVersionNotificationEnabled = finalVersionNotificationEnabled;
    }

    public boolean isBetaVersionNotificationEnabled() {
        return betaVersionNotificationEnabled;
    }

    public void setBetaVersionNotificationEnabled(boolean betaVersionNotificationEnabled) {
        this.betaVersionNotificationEnabled = betaVersionNotificationEnabled;
    }

    public boolean isSongNotificationEnabled() {
        return songNotificationEnabled;
    }

    public void setSongNotificationEnabled(boolean songNotificationEnabled) {
        this.songNotificationEnabled = songNotificationEnabled;
    }

    public Visibility getMainVisibility() {
        return mainVisibility;
    }

    public void setMainVisibility(Visibility mainVisibility) {
        this.mainVisibility = mainVisibility;
    }

    public Visibility getPlaylistVisibility() {
        return playlistVisibility;
    }

    public void setPlaylistVisibility(Visibility playlistVisibility) {
        this.playlistVisibility = playlistVisibility;
    }

    public boolean isLastFmEnabled() {
        return lastFmEnabled;
    }

    public void setLastFmEnabled(boolean lastFmEnabled) {
        this.lastFmEnabled = lastFmEnabled;
    }

    public boolean isListenBrainzEnabled() {
        return listenBrainzEnabled;
    }

    public void setListenBrainzEnabled(boolean listenBrainzEnabled) {
        this.listenBrainzEnabled = listenBrainzEnabled;
    }

    public String getLastFmUsername() {
        return lastFmUsername;
    }

    public void setLastFmUsername(String lastFmUsername) {
        this.lastFmUsername = lastFmUsername;
    }

    public String getLastFmPassword() {
        return lastFmPassword;
    }

    public void setLastFmPassword(String lastFmPassword) {
        this.lastFmPassword = lastFmPassword;
    }

    public String getListenBrainzToken() {
        return listenBrainzToken;
    }

    public void setListenBrainzToken(String listenBrainzToken) {
        this.listenBrainzToken = listenBrainzToken;
    }

    public TranscodeScheme getTranscodeScheme() {
        return transcodeScheme;
    }

    public void setTranscodeScheme(TranscodeScheme transcodeScheme) {
        this.transcodeScheme = transcodeScheme;
    }

    public int getSelectedMusicFolderId() {
        return selectedMusicFolderId;
    }

    public void setSelectedMusicFolderId(int selectedMusicFolderId) {
        this.selectedMusicFolderId = selectedMusicFolderId;
    }

    public boolean isPartyModeEnabled() {
        return partyModeEnabled;
    }

    public void setPartyModeEnabled(boolean partyModeEnabled) {
        this.partyModeEnabled = partyModeEnabled;
    }

    public boolean isNowPlayingAllowed() {
        return nowPlayingAllowed;
    }

    public void setNowPlayingAllowed(boolean nowPlayingAllowed) {
        this.nowPlayingAllowed = nowPlayingAllowed;
    }

    public boolean isAutoHidePlayQueue() {
        return autoHidePlayQueue;
    }

    public void setAutoHidePlayQueue(boolean autoHidePlayQueue) {
        this.autoHidePlayQueue = autoHidePlayQueue;
    }

    public boolean isKeyboardShortcutsEnabled() {
        return keyboardShortcutsEnabled;
    }

    public void setKeyboardShortcutsEnabled(boolean keyboardShortcutsEnabled) {
        this.keyboardShortcutsEnabled = keyboardShortcutsEnabled;
    }

    public boolean isCloseDrawer() {
        return closeDrawer;
    }

    public void setCloseDrawer(boolean closeDrawer) {
        this.closeDrawer = closeDrawer;
    }

    public boolean isClosePlayQueue() {
        return closePlayQueue;
    }

    public void setClosePlayQueue(boolean closePlayqueue) {
        this.closePlayQueue = closePlayqueue;
    }

    public boolean isAlternativeDrawer() {
        return alternativeDrawer;
    }

    public void setAlternativeDrawer(boolean alternativeDrawer) {
        this.alternativeDrawer = alternativeDrawer;
    }

    public boolean isShowIndex() {
        return showIndex;
    }

    public void setShowIndex(boolean showIndex) {
        this.showIndex = showIndex;
    }

    public boolean isAssignAccesskeyToNumber() {
        return assignAccesskeyToNumber;
    }

    public void setAssignAccesskeyToNumber(boolean assignAccesskeyToNumber) {
        this.assignAccesskeyToNumber = assignAccesskeyToNumber;
    }

    public boolean isOpenDetailIndex() {
        return openDetailIndex;
    }

    public void setOpenDetailIndex(boolean openDetailIndex) {
        this.openDetailIndex = openDetailIndex;
    }

    public boolean isOpenDetailSetting() {
        return openDetailSetting;
    }

    public void setOpenDetailSetting(boolean openDetailSetting) {
        this.openDetailSetting = openDetailSetting;
    }

    public boolean isOpenDetailStar() {
        return openDetailStar;
    }

    public void setOpenDetailStar(boolean openDetailStar) {
        this.openDetailStar = openDetailStar;
    }

    public boolean isViewAsList() {
        return viewAsList;
    }

    public void setViewAsList(boolean viewAsList) {
        this.viewAsList = viewAsList;
    }

    public AlbumListType getDefaultAlbumList() {
        return defaultAlbumList;
    }

    public void setDefaultAlbumList(AlbumListType defaultAlbumList) {
        this.defaultAlbumList = defaultAlbumList;
    }

    public AvatarScheme getAvatarScheme() {
        return avatarScheme;
    }

    public void setAvatarScheme(AvatarScheme avatarScheme) {
        this.avatarScheme = avatarScheme;
    }

    public Integer getSystemAvatarId() {
        return systemAvatarId;
    }

    public void setSystemAvatarId(Integer systemAvatarId) {
        this.systemAvatarId = systemAvatarId;
    }

    /**
     * Returns when the corresponding database entry was last changed.
     *
     * @return When the corresponding database entry was last changed.
     */
    public Date getChanged() {
        return changed;
    }

    /**
     * Sets when the corresponding database entry was last changed.
     *
     * @param changed When the corresponding database entry was last changed.
     */
    public void setChanged(Date changed) {
        this.changed = changed;
    }

    public boolean isQueueFollowingSongs() {
        return queueFollowingSongs;
    }

    public void setQueueFollowingSongs(boolean queueFollowingSongs) {
        this.queueFollowingSongs = queueFollowingSongs;
    }

    public int getPaginationSize() {
        return paginationSize;
    }

    public void setPaginationSize(int paginationSize) {
        this.paginationSize = paginationSize;
    }

    public boolean isSimpleDisplay() {
        return simpleDisplay;
    }

    public void setSimpleDisplay(boolean isSimpleDisplay) {
        this.simpleDisplay = isSimpleDisplay;
    }

    public boolean isShowSibling() {
        return showSibling;
    }

    public void setShowSibling(boolean isSiblingVisible) {
        this.showSibling = isSiblingVisible;
    }

    public boolean isShowRate() {
        return showRate;
    }

    public void setShowRate(boolean isRateVisible) {
        this.showRate = isRateVisible;
    }

    public boolean isShowAlbumSearch() {
        return showAlbumSearch;
    }

    public void setShowAlbumSearch(boolean isAlbumSearchVisible) {
        this.showAlbumSearch = isAlbumSearchVisible;
    }

    public boolean isShowLastPlay() {
        return showLastPlay;
    }

    public void setShowLastPlay(boolean isLastPlayVisible) {
        this.showLastPlay = isLastPlayVisible;
    }

    public boolean isShowDownload() {
        return showDownload;
    }

    public void setShowDownload(boolean isDownloadVisible) {
        this.showDownload = isDownloadVisible;
    }

    public boolean isShowTag() {
        return showTag;
    }

    public void setShowTag(boolean isTagVisible) {
        this.showTag = isTagVisible;
    }

    public boolean isShowComment() {
        return showComment;
    }

    public void setShowComment(boolean isCommentVisible) {
        this.showComment = isCommentVisible;
    }

    public boolean isShowShare() {
        return showShare;
    }

    public void setShowShare(boolean isShareVisible) {
        this.showShare = isShareVisible;
    }

    public boolean isShowChangeCoverArt() {
        return showChangeCoverArt;
    }

    public void setShowChangeCoverArt(boolean showChangeCoverArt) {
        this.showChangeCoverArt = showChangeCoverArt;
    }

    public boolean isShowTopSongs() {
        return showTopSongs;
    }

    public void setShowTopSongs(boolean showtopSongs) {
        this.showTopSongs = showtopSongs;
    }

    public boolean isShowSimilar() {
        return showSimilar;
    }

    public void setShowSimilar(boolean showSimilar) {
        this.showSimilar = showSimilar;
    }

    public boolean isShowAlbumActions() {
        return showAlbumActions;
    }

    public void setShowAlbumActions(boolean showAlbumActions) {
        this.showAlbumActions = showAlbumActions;
    }

    public boolean isBreadcrumbIndex() {
        return breadcrumbIndex;
    }

    public void setBreadcrumbIndex(boolean breadcrumbIndex) {
        this.breadcrumbIndex = breadcrumbIndex;
    }

    public boolean isPutMenuInDrawer() {
        return putMenuInDrawer;
    }

    public void setPutMenuInDrawer(boolean putMenuInDrawer) {
        this.putMenuInDrawer = putMenuInDrawer;
    }

    public String getFontSchemeName() {
        return fontSchemeName;
    }

    public void setFontSchemeName(String fontSchemeName) {
        this.fontSchemeName = fontSchemeName;
    }

    public boolean isShowOutlineHelp() {
        return showOutlineHelp;
    }

    public void setShowOutlineHelp(boolean showOutlineHelp) {
        this.showOutlineHelp = showOutlineHelp;
    }

    public boolean isForceBio2Eng() {
        return forceBio2Eng;
    }

    public void setForceBio2Eng(boolean forceBio2Eng) {
        this.forceBio2Eng = forceBio2Eng;
    }

    /**
     * Configuration of what information to display about a song.
     */
    public static class Visibility {
        private boolean isTrackNumberVisible;
        private boolean isArtistVisible;
        private boolean isAlbumVisible;
        private boolean isGenreVisible;
        private boolean isYearVisible;
        private boolean isBitRateVisible;
        private boolean isDurationVisible;
        private boolean isFormatVisible;
        private boolean isFileSizeVisible;
        // JP >>>>
        private boolean isComposerVisible;
        // <<<< JP

        public Visibility() {}

        public Visibility(boolean trackNumberVisible, boolean artistVisible, boolean albumVisible,
                          boolean genreVisible, boolean yearVisible, boolean bitRateVisible,
                          boolean durationVisible, boolean formatVisible, boolean fileSizeVisible,
                          // JP >>>>
                          boolean composerVisible
                          // <<<< JP
        ) {
            isTrackNumberVisible = trackNumberVisible;
            isArtistVisible = artistVisible;
            isAlbumVisible = albumVisible;
            isGenreVisible = genreVisible;
            isYearVisible = yearVisible;
            isBitRateVisible = bitRateVisible;
            isDurationVisible = durationVisible;
            isFormatVisible = formatVisible;
            isFileSizeVisible = fileSizeVisible;
            // JP >>>>
            isComposerVisible = composerVisible;
            // <<<< JP
        }

        public boolean isTrackNumberVisible() {
            return isTrackNumberVisible;
        }

        public void setTrackNumberVisible(boolean trackNumberVisible) {
            isTrackNumberVisible = trackNumberVisible;
        }

        public boolean isArtistVisible() {
            return isArtistVisible;
        }

        public void setArtistVisible(boolean artistVisible) {
            isArtistVisible = artistVisible;
        }

        public boolean isAlbumVisible() {
            return isAlbumVisible;
        }

        public void setAlbumVisible(boolean albumVisible) {
            isAlbumVisible = albumVisible;
        }

        public boolean isGenreVisible() {
            return isGenreVisible;
        }

        public void setGenreVisible(boolean genreVisible) {
            isGenreVisible = genreVisible;
        }

        public boolean isYearVisible() {
            return isYearVisible;
        }

        public void setYearVisible(boolean yearVisible) {
            isYearVisible = yearVisible;
        }

        public boolean isBitRateVisible() {
            return isBitRateVisible;
        }

        public void setBitRateVisible(boolean bitRateVisible) {
            isBitRateVisible = bitRateVisible;
        }

        public boolean isDurationVisible() {
            return isDurationVisible;
        }

        public void setDurationVisible(boolean durationVisible) {
            isDurationVisible = durationVisible;
        }

        public boolean isFormatVisible() {
            return isFormatVisible;
        }

        public void setFormatVisible(boolean formatVisible) {
            isFormatVisible = formatVisible;
        }

        public boolean isFileSizeVisible() {
            return isFileSizeVisible;
        }

        public void setFileSizeVisible(boolean fileSizeVisible) {
            isFileSizeVisible = fileSizeVisible;
        }

        public boolean isComposerVisible() {
            return isComposerVisible;
        }

        public void setComposerVisible(boolean isComposerVisible) {
            this.isComposerVisible = isComposerVisible;
        }

    }
}
