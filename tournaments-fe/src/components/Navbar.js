import React, { useState } from 'react';
import { Link } from 'react-router-dom';

const Navbar = () => {
    const [isOpen, setIsOpen] = useState(false);

    const toggleMenu = () => setIsOpen(!isOpen);

    return (
        <nav className="bg-gradient-to-r from-blue-500 to-indigo-600 p-4 shadow-lg">
            <div className="max-w-7xl mx-auto flex justify-between items-center">
                <div className="text-white font-bold text-xl">
                    <Link to="/">Turnaje</Link>
                </div>
                <div className="hidden md:flex space-x-4">
                    <Link to="/add-player" className="text-white hover:text-blue-300 transition duration-200">
                        Přidat hráče
                    </Link>
                    <Link to="/player-list" className="text-white hover:text-blue-300 transition duration-200">
                        Seznam hráčů
                    </Link>
                    <Link to="/group-setup" className="text-white hover:text-blue-300 transition duration-200">
                        Správa skupin
                    </Link>
                    <Link to="/group-results" className="block text-white hover:text-blue-300 transition duration-200">
                        Výsledky
                    </Link>
                    <Link to="/show-match" className="text-white hover:text-blue-300 transition duration-200">
                        Testovaci zobrazeni zapasu
                    </Link>
                    <Link to="/play-off" className="text-white hover:text-blue-300 transition duration-200">
                        Play-off
                    </Link>
                </div>
                <button className="text-white md:hidden" onClick={toggleMenu}>
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" className="w-6 h-6">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 6h16M4 12h16M4 18h16" />
                    </svg>
                </button>
            </div>

            {/* Mobile Menu */}
            {isOpen && (
                <div className="md:hidden bg-gradient-to-r from-blue-500 to-indigo-600 p-4 mt-2">
                    <Link to="/add-player" className="block text-white hover:text-blue-300 transition duration-200 py-2" onClick={toggleMenu}>
                        Přidat hráče
                    </Link>
                    <Link to="/player-list" className="block text-white hover:text-blue-300 transition duration-200 py-2" onClick={toggleMenu}>
                        Seznam hráčů
                    </Link>
                    <Link to="/group-setup" className="block text-white hover:text-blue-300 transition duration-200 py-2" onClick={toggleMenu}>
                        Správa skupin
                    </Link>
                    <Link to="/group-results" className="block text-white hover:text-blue-300 transition duration-200 py-2" onClick={toggleMenu}>
                        Výsledky
                    </Link>
                    <Link to="/show-match" className="block text-white hover:text-blue-300 transition duration-200 py-2" onClick={toggleMenu}>
                        Testovaci zobrazeni zapasu
                    </Link>
                    <Link to="/play-off" className="block text-white hover:text-blue-300 transition duration-200 py-2" onClick={toggleMenu}>
                        Play-off
                    </Link>
                </div>
            )}
        </nav>
    );
};

export default Navbar;
