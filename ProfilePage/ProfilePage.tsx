import React from 'react';
import './ProfilePage.css';

/* React component that renders a user's profile page
 * Includes profile picture, username, display name, bio, and posts */
function ProfilePage() {
    /* Function returns the structure of the profile page */
    return (
        /* Apply all CSS rules defined for the .container class in ProfilePage.css to this div */
        <div className="container">
            {/* Wrapper div specifically for profile content */}
            <div className="profile-page">

                {/* USERNAME */}
                {/* Div that contains the username header
                  * Apply all CSS rules defined for the profile-user-name class in ProfilePage.css to this div */}
                <div className="profile-user-name">
                    {/* Display the username at a h2 heading level
                      * Apply all CSS rules defined for the user-name class in ProfilePage.css to this h2 */}
                    <h2 className="user-name">UserName</h2>
                </div>

                {/* PROFILE PICTURE */}
                {/* Div that contains the profile picture of the user
                  * Apply all CSS rules defined for the profile-img class in ProfilePage.css to this div */}
                <div className="profile-img">
                    {/* Display the user's profile picture (an image)
                      * Dynamically adds the user's username to the alt text for better accessibility */}
                    <img
                        src="./pfp.jpg"                 /* Path to the image */
                        alt={`Missing profile picture`} /* Text that is displayed if the image cannot load */
                        width="80"                      /* Image size (width) */
                        height="80"                     /* Image size (height) */
                        draggable="false"               /* Users can’t drag and move that image within the page or to other areas */
                    />
                </div>

                {/* DISPLAY NAME */}
                {/* Div that contains the user's display name
                  * Apply all CSS rules defined for the profile-name class in ProfilePage.css to this div
                  * dir="auto" adjusts the text direction based on the language of the content */}
                <div className="profile-name" dir="auto">
                    {/* Display the display name at a h3 heading level
                      * Apply all CSS rules defined for the name class in ProfilePage.css to this h3 */}
                    <h3 className="name">DisplayName</h3>
                </div>

                {/* BIO */}
                {/*  Div that contains the user's bio/description
                  * Apply all CSS rules defined for the profile-bio class in ProfilePage.css to this div */}
                <div className="profile-bio">
                    {/* Displays the user's bio/description inside a paragraph element */}
                    <p>UserBio</p>
                </div>

                {/* POSTS */}
                {/*  Div that contains the user's posts
                  * Apply all CSS rules defined for the profile-posts-box class in ProfilePage.css to this div */}
                <div className="profile-posts-box">
                    {/* Displays a user's posts
                      * Creates a horizontal line for separation
                      * Apply all CSS rules defined for the posts class in ProfilePage.css to this hr */}
                    <hr className="posts" />
                </div>
            </div>
        </div>
    );
}

export default ProfilePage;
