---


---

<h1 id="fusy">Fusy</h1>
<h2 id="a-webserial-library-manager">A webserial library manager</h2>
<p>This project will try to implement custom APIs to <a href="https://www.royalroad.com/">royalroad.com</a>, <a href="http://japtem.com/fanfic.php">japtem</a>, <a href="https://www.fanfiction.net/">fanfiction.net</a>, <a href="https://www.wattpad.com/">wattpad</a> and other webserials and fanfiction websites.</p>
<p>The goal would be to centralise all those sources, add the ability to download the fictions offline and manage them in a library.</p>
<h2 id="wiki"><a href="https://github.com/CamilleBC/fusy/wiki">WIKI</a></h2>
<p>Checkout the <a href="https://github.com/CamilleBC/fusy/wiki">wiki</a> if you want more information about how the project’s architecture!</p>
<h2 id="updates">Updates:</h2>
<ul>
<li><a href="https://github.com/CamilleBC/fusy/commit/2801439f1cc4943a4607df2d5cf5b927e0b6a982">2019/01/08</a>
<ul>
<li>Added icons and banners to the login page, as well as managing different providers for the login.</li>
<li>Added a searchable activity that only displays favourites for now.</li>
</ul>
</li>
<li><a href="https://github.com/CamilleBC/fusy/commit/7a01a4514dca8e7182c997e50a596521e563ee44">2019/01/07</a>
<ul>
<li>The repository now saves the data to the Db and retrieves it if the host in unavailable</li>
<li>Login is managed in a separate activity</li>
<li>TODO:
<ul>
<li>configure a second host on login</li>
<li>add an Error manager to Retrofit to avoid crashes on “host unavailable”</li>
<li>save and manage royal road fiction chapters</li>
</ul>
</li>
</ul>
</li>
<li><a href="https://github.com/CamilleBC/fusy/commit/273e588588fc708cdd3dbd1852b5ea86aa22ccd2">2019/01/03</a>:
<ul>
<li>using dagger 2 to inject a singleton Fiction component that provides a FictionRepository to all activites/fragments.</li>
<li>Fiction host and database are now hidden in a Fiction repository, which will manage all interactions with the fictions.</li>
<li>started a wiki to describe the project’s architecture.</li>
</ul>
</li>
<li><a href="https://github.com/CamilleBC/fusy/commit/c4dd7b8d9de759f08e64db58bba386e260d225bd">2019/01/03</a>:
<ul>
<li>using deferred coroutines with retrofit (thanks, <a href="https://github.com/JakeWharton/retrofit2-kotlin-coroutines-adapter">Jake Wharton</a>) for the fiction host (<a href="http://RoyalRoad.com">RoyalRoad.com</a>) calls.</li>
<li>creating the beginning of a <a href="https://developer.android.com/training/data-storage/room/">Room</a> architecture for the fiction db/repository.</li>
<li>creating the beginning of a <a href="https://google.github.io/dagger/">dagger2</a> dependency injection architecture for the fiction db/repository.</li>
<li>add a HardwareStatusManager that uses the global App context to retrive the network and battery status. TODO: implement a broadcast listener to get the <a href="https://developer.android.com/reference/android/content/Intent.html#ACTION_BATTERY_LOW">ACTION_BATTERY_LOW</a> intent broadcast.</li>
</ul>
</li>
<li><strong>2018/12/05</strong>:
<ul>
<li>add a diagram to explain the FictionComponent architecture.</li>
</ul>
</li>
<li><a href="https://github.com/CamilleBC/fusy/commit/76173f3b7ca6f2c4dd43769217421a798013fa5f">2018/12/03</a>:
<ul>
<li>using <a href="https://bumptech.github.io/glide/">Glide</a> to manage image loadings.</li>
</ul>
</li>
</ul>
<blockquote>
<p>Written with <a href="https://stackedit.io/">StackEdit</a>.</p>
</blockquote>

