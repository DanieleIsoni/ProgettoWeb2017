
<div id="footer">
    <div id="footer_inner">
    &#9400; BuyHub Inc. 2017
    &nbsp; P.I. 123412312312
    &nbsp; <a href="<c:url value="disclaimer.jsp"/>"><fmt:message key="disclaimer_title"/></a>
    </div>
</div>

<link rel="stylesheet" type="text/css" href="//cdnjs.cloudflare.com/ajax/libs/cookieconsent2/3.0.3/cookieconsent.min.css" />
<script src="//cdnjs.cloudflare.com/ajax/libs/cookieconsent2/3.0.3/cookieconsent.min.js"></script>
<script>
window.addEventListener("load", function(){
window.cookieconsent.initialise({
  "palette": {
    "popup": {
      "background": "#edeff5",
      "text": "#838391"
    },
    "button": {
      "background": "#4b81e8"
    }
  },
  "theme": "edgeless",
  "position": "bottom-left",
  "content": {
    "message": "<fmt:message key="disclaimer_cookie"/>",
    "href": "<c:url value="disclaimer.jsp"/>",
    "dismiss": "<fmt:message key="cookie_btn"/>",
    "link": "<fmt:message key="cookie_more"/>"
  }
})});
</script>
    </body>
</html>
