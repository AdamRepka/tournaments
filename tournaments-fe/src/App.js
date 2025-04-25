import React, { useState } from "react";
import AddPlayerForm from "./components/AddPlayerForm";

function App() {
  const [players, setPlayers] = useState([]);

  const handleAddPlayer = (name) => {
    setPlayers((prev) => [...prev, name]);
  };

  return (
      <div className="min-h-screen bg-gray-100 p-8">
        <AddPlayerForm onAddPlayer={handleAddPlayer} />
        <div className="mt-8 max-w-md mx-auto bg-white p-4 rounded shadow">
          <h3 className="text-lg font-semibold mb-2">Seznam hráčů:</h3>
          <ul className="list-disc pl-5">
            {players.map((player, index) => (
                <li key={index}>{player}</li>
            ))}
          </ul>
        </div>
      </div>
  );
}

export default App;
