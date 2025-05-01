import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Navbar from './components/Navbar'; // Import navbaru
import Home from './pages/Home'; // Import Home stránky
import AddPlayer from './pages/AddPlayer'; // Import stránky pro přidání hráče
import PlayerList from './pages/PlayerList'; // Import stránky pro seznam hráčů

function App() {
    return (
        <Router>
            <Navbar /> {/* Zde přidáš komponentu Navbar */}
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/add-player" element={<AddPlayer />} />
                <Route path="/player-list" element={<PlayerList />} />
            </Routes>
        </Router>
    );
}

export default App;
