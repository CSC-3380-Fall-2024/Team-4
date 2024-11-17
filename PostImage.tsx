//tutorial:
//https://dev.to/shieldstring/upload-images-videos-and-audio-in-react-js-using-cloudinary-3ji
//discussion about creating fetch requests to a cloud
//https://community.cloudinary.com/discussion/116/is-it-safe-to-have-cloud-name-and-preset-in-front-end-code-upload-question
//version1

import React, { useState, FC} from "react";

/* React component that allows a user to upload an image and display it */
const Input: React.FC = function () {
    /* "image" holds the value of the uploaded image URL
     * "image" is initialized to an empty string
     * "setImage" updates the state of the component
        * when an image is uploaded, the URL of the image is returned
        * "image" gets updated and will contain the URL of the uploaded image
    * Whenever setImage is called, the component updates the `image` state */
    const [image, setImage] = useState<string>("");

    /* Function that handles the image uploading process
     * Checks if "files" is null or contains files
     * Iterates over the files in the FileList to upload each file
     * The function does not return any value */
    function uploadImage(files: FileList | null): void {
        /* If "files" is null, condition is not satisfied
         * If files exists but contains no files (files.length === 0), condition is not satisfied
         * If files exists and has at least one file, condition is satisfied
         * IntelliJ made me add this */
        if (files && files.length > 0) {

            /* Create a new FormData object to hold the file data
             * "A FormData object is used to build key-value pairs representing form fields and their values" */
            const formData = new FormData();

            /* Takes the first file in the FileList (files[0]) and adds it to the FormData object under the key "file"
             * Prepares the file to be sent to a server as part of a request*/
            formData.append("file", files[0]);

            /* An upload preset is a configuration that defines specific settings for uploads
             * This adds a key-value pair to the FormData object:
                * key = "upload_preset"
                * value = "<your upload preset>" (placeholder) */
            formData.append("upload_preset", "<your upload preset>");

            /* Send a request to an endpoint (currently a placeholder url)*/
            fetch("https://api.cloudinary.com/v1_1/<your cloud name>/image/upload", {
                /* Specifies the type of request */
                method: "POST",
                /* The body of the request is the FormData object */
                body: formData,
            })
                /* After uploading the image, this function
                 * ensures the response is in JSON format */
                .then(function (response) {
                    return response.json();
                })

                /* If the upload is successful/promise is fulfilled, the API response will contain a "secure_url"
                 * The "secure_url" points to the uploaded image
                 * Call setImage to update the status of "image" */
                .then(function (data) {
                    setImage(data.secure_url);
                })
        }
    }

    return <div>
        {/* Input field allows the user to select a file
          * The `onChange` event is triggered when a file is selected
          * The file is then passed to the `uploadImage` function */}
        <input type="file onChange={(e) => uploadImage(e.target.files)}"></input>
        {/* Img tag displays the contents
          * Grabs the URL from the `image` state that got updated to display the image */}
        <img
            src={image}
        />
    </div>;
}
export default Input;