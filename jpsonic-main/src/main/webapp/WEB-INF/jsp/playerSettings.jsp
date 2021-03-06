<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="iso-8859-1" %>
<%--@elvariable id="command" type="org.airsonic.player.command.PlayerSettingsCommand"--%>

<html><head>
    <%@ include file="head.jsp" %>
    <%@ include file="jquery.jsp" %>
    <script src="<c:url value='/script/utils.js'/>"></script>
</head>
<body class="mainframe settings playerSettings">

<script lang="javascript">

var viewNoJavaJukes;
var viewJavaJukes;

function hideAllTechnologyDepends() {
    viewJavaJukes.forEach(c => c.style.display = "none");
    viewNoJavaJukes.forEach(c => c.style.display = "none");
}

function showTechnologyDepends(technologyName) {
  var selector = Array.from(document.getElementsByClassName('.technologyDepends.' + technologyName));
  switch (technologyName) {
    case "WEB":
    case "EXTERNAL":
    case "EXTERNAL_WITH_PLAYLIST":
    case "JUKEBOX":
      viewNoJavaJukes.forEach(c => c.style.display = "");
      viewJavaJukes.forEach(c => c.style.display = "none");
      break;
    case "JAVA_JUKEBOX":
      viewNoJavaJukes.forEach(c => c.style.display = "none");
      viewJavaJukes.forEach(c => c.style.display = "");
      break;
  }
}

$(document).ready(function() {
  viewNoJavaJukes = Array.from(document.getElementsByClassName('noJavaJuke'));
  viewJavaJukes = Array.from(document.getElementsByClassName('javaJuke'));
  $('.technologyRadio').click(function() {
    hideAllTechnologyDepends();
    var technologyName = $(this).val();
    showTechnologyDepends(technologyName);
  });

  hideAllTechnologyDepends();
  $('.technologyRadio:checked').each(function() {
    var technologyName = $(this).val();
    showTechnologyDepends(technologyName);
  });
});

</script>

<c:import url="settingsHeader.jsp">
    <c:param name="cat" value="player"/>
    <c:param name="toast" value="${command.showToast}"/>
    <c:param name="restricted" value="${not command.admin}"/>
    <c:param name="useRadio" value="${command.useRadio}"/>
    <c:param name="useSonos" value="${command.useSonos}"/>
</c:import>

<fmt:message key="common.unknown" var="unknown"/>

<c:choose>
    <c:when test="${empty command.players}">
        <p><fmt:message key="playersettings.noplayers"/></p>
    </c:when>
    <c:otherwise>

        <div class="titledSelector player">
            <fmt:message key="playersettings.title"/>
            <select name="player" onchange="location='playerSettings.view?id=' + options[selectedIndex].value;">
                <c:forEach items="${command.players}" var="player">
                    <option ${player.id eq command.playerId ? "selected" : ""}
                            value="${player.id}">${fn:escapeXml(player.description)}</option>
                </c:forEach>
            </select>
        </div>

        <form:form modelAttribute="command" method="post" action="playerSettings.view">
            <form:hidden path="playerId"/>
            <details open>
                <summary class="legacy"><fmt:message key="playersettings.settings"/></summary>

                <dl>
                    <dt><fmt:message key="playersettings.type"/></dt>
                    <dd>
                        <c:choose>
                            <c:when test="${empty command.type}">${unknown}</c:when>
                            <c:otherwise>${command.type}</c:otherwise>
                        </c:choose>
                    </dd>
                    <dt><fmt:message key="playersettings.name"/></dt>
                    <dd><form:input path="name" value="${command.name}"/><c:import url="helpToolTip.jsp"><c:param name="topic" value="playername"/></c:import></dd>
                    <dt><fmt:message key="playersettings.devices"/></dt>
                    <dd>
                        <ul class="playerSettings">
                            <c:forEach items="${command.technologyHolders}" var="technologyHolder">
                                <c:set var="technologyName">
                                    <fmt:message key="playersettings.technology.${fn:toLowerCase(technologyHolder.name)}"/>
                                </c:set>
                                <li>
                                    <form:radiobutton class="technologyRadio" id="radio-${technologyName}" path="technologyName" value="${technologyHolder.name}"/>
                                    <label for="radio-${technologyName}">${technologyName}</label>
                                    <c:import url="helpToolTip.jsp"><c:param name="topic" value="playersettings.technology.${fn:toLowerCase(technologyHolder.name)}"/></c:import>
                                </li>
                            </c:forEach>
                        </ul>
                    </dd>
                    <dt class="noJavaJuke"><fmt:message key="playersettings.maxbitrate"/></dt>
                    <dd class="noJavaJuke">
                        <form:select path="transcodeSchemeName">
                            <c:forEach items="${command.transcodeSchemeHolders}" var="transcodeSchemeHolder">
                                <form:option value="${transcodeSchemeHolder.name}" label="${transcodeSchemeHolder.description}"/>
                            </c:forEach>
                        </form:select>
                        <c:import url="helpToolTip.jsp"><c:param name="topic" value="transcode"/></c:import>
                        <c:if test="${not command.transcodingSupported}">
                            <strong><fmt:message key="playersettings.notranscoder"/></strong>
                        </c:if>
                    </dd>
                    <c:if test="${not empty command.allTranscodings}">
                        <dt class="noJavaJuke"><fmt:message key="playersettings.transcodings"/></dt>
                        <dd class="noJavaJuke">
                            <c:forEach items="${command.allTranscodings}" var="transcoding" varStatus="loopStatus">
                                <form:checkbox path="activeTranscodingIds" id="transcoding${transcoding.id}" value="${transcoding.id}" cssClass="checkbox"/>
                                <label for="transcoding${transcoding.id}">${transcoding.name}</label>
                            </c:forEach>
                        </dd>
                    </c:if>
                    <dt class="noJavaJuke"></dt>
                    <dd class="noJavaJuke">
                        <form:checkbox path="dynamicIp" id="dynamicIp" cssClass="checkbox"/>
                        <label for="dynamicIp"><fmt:message key="playersettings.dynamicip"/></label>
                        <c:import url="helpToolTip.jsp"><c:param name="topic" value="dynamicip"/></c:import>
                    </dd>
                    <dt class="noJavaJuke"></dt>
                    <dd class="noJavaJuke">
                        <form:checkbox path="autoControlEnabled" id="autoControlEnabled" cssClass="checkbox"/>
                        <label for="autoControlEnabled"><fmt:message key="playersettings.autocontrol"/></label>
                        <c:import url="helpToolTip.jsp"><c:param name="topic" value="autocontrol"/></c:import>
                    </dd>
                    <dt class="noJavaJuke"></dt>
                    <dd class="noJavaJuke">
                        <form:checkbox path="m3uBomEnabled" id="m3uBomEnabled" cssClass="checkbox"/>
                        <label for="m3uBomEnabled"><fmt:message key="playersettings.m3ubom"/></label>
                        <c:import url="helpToolTip.jsp"><c:param name="topic" value="m3ubom"/></c:import>
                    </dd>
                    <dt class="javaJuke"><fmt:message key="playersettings.javaJukeboxMixer"/></dt>
                    <dd class="javaJuke">
                        <form:select path="javaJukeboxMixer">
                            <c:forEach items="${command.javaJukeboxMixers}" var="javaJukeboxMixer">
                                <form:option value="${javaJukeboxMixer}" label="${javaJukeboxMixer}"/>
                            </c:forEach>
                        </form:select>
                    </dd>
                    <dt><fmt:message key="playersettings.lastseen"/></dt>
                    <dd><fmt:formatDate value="${command.lastSeen}" type="both" dateStyle="long" timeStyle="medium"/></dd>
                </dl>
            </details>
    
            <c:url value="playerSettings.view" var="deleteUrl">
                <c:param name="delete" value="${command.playerId}"/>
            </c:url>
            <c:url value="playerSettings.view" var="cloneUrl">
                <c:param name="clone" value="${command.playerId}"/>
            </c:url>

            <c:set var="isOpen" value='${command.openDetailSetting ? "open" : ""}' />
            <details ${isOpen}>
                <summary><fmt:message key="playersettings.deleteandclone"/></summary>
                <dl>
                    <dt><fmt:message key="playersettings.forget"/></dt>
                    <dd><div><input type="button" onClick="location.href='${deleteUrl}';window.top.playQueue.location.reload()" value="<fmt:message key='playersettings.forgetplayer'/>"/></div></dd>       
                    <dt><fmt:message key="playersettings.clone"/></dt>
                    <dd><div><input type="button" onClick="location.href='${cloneUrl}';window.top.playQueue.location.reload()" value="<fmt:message key='playersettings.cloneplayer'/>"/></div></dd>
                </dl>
            </details>

            <div class="submits">
                <input type="submit" value="<fmt:message key='common.save'/>">
                <input type="button" onClick="location.href='nowPlaying.view'" value="<fmt:message key='common.cancel'/>"/>
            </div>

        </form:form>
    </c:otherwise>
</c:choose>

<c:if test="${settings_reload}">
    <script>
      window.top.reloadUpper("playerSettings.view");
      window.top.reloadPlayQueue();
      window.top.reloadRight();
    </script>
</c:if>

</body></html>
