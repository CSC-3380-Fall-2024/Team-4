import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';

import Homefeed from './pages/Homefeed';
import Explore from './pages/Explore';
import Nav from './components/Nav/Nav';
import Upload from './pages/Upload';

import './styles/Explore.css';
import './styles/App.css';

const App: React.FC = () => {
  return (
    <Router>
      <div className="App">
        <Nav />
        <div className="content-container">
          <Routes>
            <Route path="/" element={<Navigate to="/home" />} />
            <Route path="/home" element={<Homefeed />} />
            <Route path="/explore" element={<Explore />} />
            <Route path="/upload" element={<Upload />} />
          </Routes>
        </div>
      </div>
    </Router>
  );
};

export default App;
