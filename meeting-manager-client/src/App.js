import './App.css';
import 'primereact/resources/primereact.min.css';
import 'primereact/resources/themes/omega/theme.css';
import 'font-awesome/css/font-awesome.css';

import React, { Component } from 'react';
import Employee from "./pages/Employee"
import Department from "./pages/Department"
import Meeting from "./pages/Meeting"

import {Route, Link, Switch} from "react-router-dom"

class App extends Component {
  render() {
    return (
        <div>
            <div id="menu">
                <SideMenu/>
            </div>
            <div className="ui-g-3"></div>
            <div className="ui-g-6">
                <Switch>
                    <Route path = "/index" component ={Index}/>
                    <Route path = "/meeting" component ={Meeting}/>
                    <Route path = "/department" component ={Department}/>
                    <Route path = "/employee" component ={Employee}/>
                </Switch>
            </div>
        </div>
    );
  }
}

export default App;

function SideMenu(propes) {
    return (
        <ul>
            <li><Link to="index">Home</Link></li>
            <li><Link to="meeting">Meetings</Link></li>
            <li><Link to="department">Departments</Link></li>
            <li><Link to="employee">Employees</Link></li>
        </ul>
    );
}

function Index(props) {
    return <h1>Welcome To Meeing Manager!</h1>;
}
