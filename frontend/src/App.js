import React from 'react';
import './App.css';
import Main from "./component/Main";
import {BrowserRouter as Router, Route, useLocation} from "react-router-dom";
import {Switch} from "react-bootstrap";
import VoteResult from "./component/VoteResult";

function App() {
  return (
    <div className="container">
      <header className="App-header">
          <RouterFunction />
       {/*<Poll pollId="1" />*/}
      </header>
    </div>
  );
}

function RouterFunction() {
    return (
        <Router>
            <SwitchFunction />
        </Router>
    )

}

function SwitchFunction() {
    let location = useLocation();

    return (
        <div>
            <Switch location={location}>
                <Route exact path="/">{<Main />}</Route>
                <Route path="/computeresult/:pollId" children={<VoteResult />} />
            </Switch>
        </div>
    )
}
export default App;
