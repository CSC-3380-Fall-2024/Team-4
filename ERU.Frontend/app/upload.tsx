import React, { useState, ChangeEvent, FormEvent } from 'react';

import Nav from './components/Nav/Nav';

const Upload: React.FC = () => {
  const [caption, setCaption] = useState<string>('');
  const [image, setImage] = useState<File | null>(null);
  const [uploading, setUploading] = useState<boolean>(false);
  const [uploadSuccess, setUploadSuccess] = useState<boolean>(false);
  const [errorMessage, setErrorMessage] = useState<string>('');

  const handleImageChange = (e: ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      setImage(e.target.files[0]);
    }
  };

  const convertFileToBase64 = (file: File): Promise<string> => {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => resolve(reader.result as string);
      reader.onerror = (error) => reject(error);
    });
  };

  const handleUpload = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (!caption || !image) {
      setErrorMessage('Please fill in all fields and select an image.');
      return;
    }

    setUploading(true);
    setErrorMessage('');
    setUploadSuccess(false);

    try {
      const base64Image = await convertFileToBase64(image);

      const postData = {
        content: base64Image,
        caption: caption,
        likes: [],
        comments: [],
        owning_user: "Anonymous",
        owning_picture: "NAN",
        identifier: "NAN",
      };

      const response = await fetch('http://127.0.0.1:7877/account/post', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(postData),
      });

      if (!response.ok) {
        throw new Error('Failed to upload post');
      }

      setUploading(false);
      setUploadSuccess(true);
      setCaption('');
      setImage(null);
    } catch (error) {
      console.error('Error uploading post:', error);
      setErrorMessage('An error occurred. Please try again.');
      setUploading(false);
    }
  };

  return (
    <div className="upload-page" style={{ maxWidth: '600px', margin: '0 auto', padding: '20px' }}>
      <Nav/>
      <h2>Upload a New Post</h2>
      {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>}
      {uploadSuccess && <p style={{ color: 'green' }}>Post uploaded successfully!</p>}

      <form onSubmit={handleUpload} style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
        <label>
          Caption:
          <textarea
            value={caption}
            onChange={(e) => setCaption(e.target.value)}
            required
            style={{ padding: '8px', fontSize: '14px' }}
          />
        </label>

        <label>
          Image:
          <input
            type="file"
            accept="image/*"
            onChange={handleImageChange}
            required
          />
        </label>

        <button
          type="submit"
          disabled={uploading}
          style={{ padding: '10px', fontSize: '16px', cursor: 'pointer' }}
        >
          {uploading ? 'Uploading...' : 'Upload Post'}
        </button>
      </form>
    </div>
  );
};

export default Upload;
