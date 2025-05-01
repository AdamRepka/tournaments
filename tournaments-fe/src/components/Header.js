import React from 'react';
import { Link } from 'react-router-dom';

const Header = () => {
    return (
        <header className="bg-blue-500 text-white p-4">
            <nav>
                <ul className="flex space-x-4">
                    <li><Link to="/" className="hover:text-gray-300">Home</Link></li>
                    <li><Link to="/players" className="hover:text-gray-300">Players</Link></li>
                    <li><Link to="/matches" className="hover:text-gray-300">Matches</Link></li>
                </ul>
            </nav>
        </header>
    );
};

export default Header;
