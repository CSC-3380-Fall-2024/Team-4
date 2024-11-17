// import React, { useState, FC } from "react";

// /* SaveButton manages and displays the status of "bookmarked" */
// const SaveButton: FC = function () {

//     /* "bookmarked" is the current status of the post (saved/un-saved), set to "false" (un-saved) by default
//     * setBookmarked is a function that updates the status of bookmarked
//     * Whenever setBookmarked is called, the component re-renders with the new "bookmarked" status */
//     const [bookmarked, setBookmarked] = useState<boolean>(false);

//     /* toggleBookmark checks if "bookmarked" is true
//     * If true, it means the item is already saved
//     * So, when the save button is clicked a second time, it will un-save the post
//     * If false, then the item isn't saved yet
//     * So, when the save button is clicked, setBookmarked will be set to true
//     * Function is void because it doesn't return a value */
//     function toggleBookmark(): void {
//         setBookmarked(!bookmarked);
//     }

//     // getBookmarkText returns appropriate text based on bookmark status

//     /* getBookmarkText returns appropriate text based on bookmark status
//     * if bookmarked is true, show "Un-save"
//     * if bookmarked is false, show "Save" */
//     function getBookmarkText(): string {
//         if (bookmarked) {
//             return "un-save";
//         } else {
//             return "save";
//         }
//     }

//     return (
//         <div>
//             {/* There is a button that will display getBookmarkText
//               * When clicked the button will call the function toggleBookmark */}
//             <button onClick={toggleBookmark}>{getBookmarkText()}</button>
//         </div>
//     );
// }

// export default SaveButton;