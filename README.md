# Popular Movies, Stage 2
When complete this will be my submission for the second project in the Google/Udacity "Associate Android Developer Fast Track" course. I'm pleased to say that everything is now working -- I just need to do some more cleanup before submitting! :-)

### Common Project Requirements
Meets Specifications:

* [x] App is written solely in the Java Programming Language.
* [ ] App conforms to common standards found in the Android Nanodegree General Project Guidelines.

### User Interface - Layout
Meets Specifications:

* [x] UI contains an element (e.g., a spinner or settings menu) to toggle the sort order of the movies by: most popular, highest rated.
* [x] Movies are displayed in the main layout via a grid of their corresponding movie poster thumbnails.
* [x] UI contains a screen for displaying the details for a selected movie.
* [x] Movie Details layout contains title, release date, movie poster, vote average, and plot synopsis.
* [x] Movie Details layout contains a section for displaying trailer videos and user reviews.

### User Interface - Function
Meets Specifications:

* [x] When a user changes the sort criteria (most popular, highest rated, and favorites) the main view gets updated correctly.
* [x] When a movie poster thumbnail is selected, the movie details screen is launched.
* [x] When a trailer is selected, app uses an Intent to launch the trailer.
* [x] In the movies detail screen, a user can tap a button(for example, a star) to mark it as a Favorite.

### Network API Implementation
Meets Specifications:

* [x] In a background thread, app queries the /movie/popular or /movie/top_rated API for the sort criteria specified in the settings menu.
* [x] App requests for trailers for a selected movie via the /movie/{id}/videos endpoint in a background thread and displays those details when the user selects a movie.
* [x] App requests for user reviews for a selected movie via the /movie/{id}/reviews endpoint in a background thread and displays those details when the user selects a movie.

### Data Persistence
Meets Specifications:

* [x] The titles and ids of the user's favorite movies are stored in a ContentProvider backed by a SQLite database. This ContentProvider is updated whenever the user favorites or unfavorites a movie.
* [x] When the "favorites" setting option is selected, the main view displays the entire favorites collection based on movie ids stored in the ContentProvider.

### Notes
To run this app, you will need to replace "your API key here" in the following section of AndroidManifest.xml --
```
        <meta-data
                android:name="TMDB_API_KEY"
                android:value="your API key here"/>
```
-- with your own TMDb API key.

### Required TMDb attribution

<img src="https://www.themoviedb.org/assets/9b3f9c24d9fd5f297ae433eb33d93514/images/v4/logos/408x161-powered-by-rectangle-green.png" width="408px" height="161px" align="right">(As my app doesn't at the moment have an "'About' or 'Credits. type section" I thought I would just put this in the readme for now.) From https://www.themoviedb.org/faq/api : "You shall use the TMDb logo to identify your use of the TMDb APIs. You shall place the following notice prominently on your application: 'This product uses the TMDb API but is not endorsed or certified by TMDb.' Any use of the TMDb logo in your application shall be less prominent than the logo or mark that primarily describes the application and your use of the TMDb logo shall not imply any endorsement by TMDb. When attributing TMDb, the attribution must be within your application's 'About' or 'Credits. type section."
