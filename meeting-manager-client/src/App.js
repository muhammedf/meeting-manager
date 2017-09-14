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
            <SideMenu/>
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
            <Link to = "/index">
        <li>index</li>
            </Link>
            <Link to = "/meeting">
                <li>meeting</li>
            </Link>
            <Link to = "/department">
        <li>department</li>
            </Link>
            <Link to = "/employee">
        <li>employee</li>
            </Link>
        </ul>
    );
}

function Index(props) {
    return <h1>INDEX</h1>;
}
