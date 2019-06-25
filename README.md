# Fusy

## A webserial library manager

This project will try to implement custom APIs to [royalroad.com](https://www.royalroad.com/), [japtem](http://japtem.com/fanfic.php), [fanfiction.net](https://www.fanfiction.net/), [wattpad](https://www.wattpad.com/) and other webserials and fanfiction websites.

The goal would be to centralise all those sources, add the ability to download the fictions offline and manage them in a library.

## [WIKI](https://github.com/CamilleBC/fusy/wiki)
Checkout the [wiki](https://github.com/CamilleBC/fusy/wiki) if you want more information about how the project's architecture!

## Updates:
 - [2019/01/08](https://github.com/CamilleBC/fusy/commit/2801439f1cc4943a4607df2d5cf5b927e0b6a982)
     - Added icons and banners to the login page, as well as managing different providers for the login.
     - Added a searchable activity that only displays favourites for now.
 - [2019/01/07](https://github.com/CamilleBC/fusy/commit/7a01a4514dca8e7182c997e50a596521e563ee44)
	 - The repository now saves the data to the Db and retrieves it if the host in unavailable
	 - Login is managed in a separate activity
	 - TODO: 
		 - configure a second host on login
		 - add an Error manager to Retrofit to avoid crashes on "host unavailable"
		 - save and manage royal road fictionForDb chapters
 - [2019/01/03](https://github.com/CamilleBC/fusy/commit/273e588588fc708cdd3dbd1852b5ea86aa22ccd2):
	 - using dagger 2 to inject a singleton Fiction component that provides a FictionRepository to all activites/fragments.
	 - Fiction host and database are now hidden in a Fiction repository, which will manage all interactions with the fictions. 
	 - started a wiki to describe the project's architecture.
 - [2019/01/03](https://github.com/CamilleBC/fusy/commit/c4dd7b8d9de759f08e64db58bba386e260d225bd):
	 - using deferred coroutines with retrofit (thanks, [Jake Wharton](https://github.com/JakeWharton/retrofit2-kotlin-coroutines-adapter)) for the fictionForDb host (RoyalRoad.com) calls.
	 - creating the beginning of a [Room](https://developer.android.com/training/data-storage/room/) architecture for the fictionForDb db/repository.
	 - creating the beginning of a [dagger2](https://google.github.io/dagger/) dependency injection architecture for the fictionForDb db/repository.
	 - add a HardwareStatusManager that uses the global App context to retrive the network and battery status. TODO: implement a broadcast listener to get the [ACTION_BATTERY_LOW](https://developer.android.com/reference/android/content/Intent.html#ACTION_BATTERY_LOW) intent broadcast.
 - __2018/12/05__:
	 - add a diagram to explain the FictionComponent architecture. 
 - [2018/12/03](https://github.com/CamilleBC/fusy/commit/76173f3b7ca6f2c4dd43769217421a798013fa5f):
	 - using [Glide](https://bumptech.github.io/glide/) to manage image loadings.

## Credits

**open-book** and **bookshelf** icons made by [**Freepik**](https://www.freepik.com/home) from **_[www.flaticon.com](http://www.flaticon.com/)_**


Readme written with [**StackEdit**](https://stackedit.io/).
