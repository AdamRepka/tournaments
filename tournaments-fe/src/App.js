import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Navbar from './components/Navbar'; // Import navbaru
import Home from './pages/Home'; // Import Home stránky
import AddPlayer from './pages/AddPlayer'; // Import stránky pro přidání hráče
import PlayerList from './pages/PlayerList';
import EditPlayer from "./components/EditPlayer";
import GroupSetup from "./pages/GroupSetup";
import GroupResults from "./pages/GroupResults";
import ShowMatch from "./pages/ShowMatch";
import PlayOff from "./pages/PlayOff"; // Import stránky pro seznam hráčů

function App() {
    return (
        <Router>
            <Navbar /> {/* Zde přidáš komponentu Navbar */}
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/add-player" element={<AddPlayer />} />
                <Route path="/player-list" element={<PlayerList />} />
                <Route path="/players/edit/:id" element={<EditPlayer />} />
                <Route path="/group-setup" element={<GroupSetup />} />
                <Route path="/group-results" element={<GroupResults />} />/
                <Route path="/show-match" element={<ShowMatch />} />
                <Route path="/play-off" element={<PlayOff />} />
            </Routes>
        </Router>
    );
}

export default App;
