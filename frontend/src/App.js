import React from 'react';
import './App.css';
import Poll from "./component/Poll";
import Main from "./component/Main";

function App() {
  return (
    <div className="container">
      <header className="App-header">
          <Main />
       {/*<Poll pollId="1" />*/}
      </header>
    </div>
  );
}

export default App;
