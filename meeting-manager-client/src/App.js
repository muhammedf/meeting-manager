import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';

import {Route, Link, Switch} from "react-router-dom"

class App extends Component {
  render() {
    return (
        <div>
            <SideMenu/>
            <Switch>
                <Route path = "/index" component ={Index}/>
                <Route path = "/meeting" component ={Meeting}/>
                <Route path = "/emp" component ={Employee}/>
            </Switch>
        </div>
    );
  }
}

export default App;

function SideMenu(propes) {
    return (
        <ul>
            <Link to = "/index">
        <li>index</li>
            </Link>
            <Link to = "/meeting">
        <li>meeting</li>
            </Link>
            <Link to = "/emp">
        <li>employee</li>
            </Link>
        </ul>
    );
}

function Index(props) {
    return <h1>INDEX</h1>;
}

function Meeting(props) {
    return <h1>MEETING</h1>;
}

function Employee(props) {
    return <h1>EMPLOYEE</h1>;
}



