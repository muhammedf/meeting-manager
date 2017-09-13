import React from "react"

import {DataTable} from "primereact/components/datatable/DataTable"
import {Column} from "primereact/components/column/Column"
import {Button} from "primereact/components/button/Button"
import {Dialog} from "primereact/components/dialog/Dialog"
import {InputText} from "primereact/components/inputtext/InputText"
import {MultiSelect} from 'primereact/components/multiselect/MultiSelect';

import {DepApi} from "../api/ApiObject"
import {EmpApi} from "../api/ApiObject";

export default class Department extends React.Component{

    constructor() {
        super();
        this.state = {};
        this.save = this.save.bind(this);
        this.delete = this.delete.bind(this);
        this.onDepSelect = this.onDepSelect.bind(this);
        this.addNew = this.addNew.bind(this);

        this.allEmployees=[];
    }

    componentDidMount() {
        DepApi.listall().end((err, res) => this.setState({deps: res.body}));
    }

    save() {
        let deps = [...this.state.deps];
        let dep = this.state.dep
        if(this.newDep)
            DepApi.create(this.state.dep).end((err, res) => {
                dep.id=res.body;
                deps.push(dep);
                this.setState({deps:deps});
            });
        else{
            deps[this.findSelectedDepIndex()] = this.state.dep;
            DepApi.update(this.state.dep).end();
        }

        this.setState({deps:deps, selectedDep:null, dep: null, displayDialog:false});
    }

    delete() {
        let index = this.findSelectedDepIndex();
        DepApi.delete(this.state.selectedDep.id).end((err, res) =>{
            this.setState({
                deps: this.state.deps.filter((val,i) => i !== index),
                selectedDep: null,
                dep: null,
                displayDialog: false});
        })
    }

    findSelectedDepIndex(){
        return this.state.deps.indexOf(this.state.selectedDep);
    }

    updateProperty(property, value) {
        let dep = this.state.dep;
        dep[property] = value;
        this.setState({dep: dep});
    }

    onDepSelect(e){
        this.newDep = false;
        this.setState({
            displayDialog:true,
            dep: Object.assign({}, e.data),
            employees: [],
        });
        this.allEmployeesFetched=false;
        this.myEmployeesFetched=false;
    }

    addNew() {
        this.newDep = true;
        this.setState({
            dep: {name:'', description: ''},
            displayDialog: true
        });
    }

    listMyEmployees(){
        if(!this.myEmployeesFetched){
            DepApi.listEmployees(this.state.dep.id).end((err, res) => this.setState({employees: res.body, myEmployeesFetched: true}));
        }
        this.setState({displayMyEmployeeDialog: true})
    }

    listAllEmployees(){
        if(!this.allEmployeesFetched){
            EmpApi.listall().end((err, res) => {
                var diff = res.body.filter(x => this.state.employees.every((elem, index, array) => elem.id !=x.id));
                window.diff=diff;
                this.allEmployees=diff;
                this.setState({displayAllEmployeeDialog: true});
            });
        }
    }

    addEmployees(){
        let employees=this.state.employees;
        this.state.newEmployees.forEach( ne =>
            DepApi.addEmployee(this.state.dep.id, ne.id).end((err, res) => {
                if(employees.every((elem, index, array) => elem.id != ne.id)) employees.push(ne);
                this.setState({employees: employees});
            })
        )
        this.setState({displayAllEmployeeDialog: false});
    }

    removeEmployees(){
        let employees=this.state.selectedEmployees;
        employees.forEach(e => DepApi.removeEmployee(this.state.dep.id, e.id).end((err, res) =>{
            let i = this.state.employees.indexOf(e);
            this.setState({employees: this.state.employees.filter(emp => emp.id != e.id)});
        }))
    }

    render(){

        let header = <div className="ui-helper-clearfix" style={{lineHeight:'1.87em'}}>Departments</div>;

        let footer = <div className="ui-helper-clearfix" style={{width:'100%'}}>
            <Button style={{float:'left'}} icon="fa-plus" label="Add" onClick={this.addNew}/>
        </div>;

        let dialogFooter = <div className="ui-dialog-buttonpane ui-helper-clearfix">
            <Button icon="fa-close" label="Delete" onClick={this.delete}/>
            <Button label="Save" icon="fa-check" onClick={this.save}/>
        </div>;

        return (<div className="content-section implementation">
            <DataTable value={this.state.deps} paginator={true} rows={15}  header={header} footer={footer}
                       selectionMode="single" selection={this.state.selectedDep} onSelectionChange={(e)=>{this.setState({selectedDep:e.data});}}
                       onRowSelect={this.onDepSelect}>
                <Column field="name" header="Name" sortable={true} />
                <Column field="description" header="Description" sortable={false} />
            </DataTable>

            <Dialog visible={this.state.displayDialog} header="Dep Details" modal={true} footer={dialogFooter} onHide={() => this.setState({displayDialog: false})}>
                {this.state.dep && <div className="ui-grid ui-grid-responsive ui-fluid">
                    <div className="ui-grid-row">
                        <div className="ui-grid-col-4" style={{padding:'4px 10px'}}><label htmlFor="name">Name</label></div>
                        <div className="ui-grid-col-8" style={{padding:'4px 10px'}}>
                            <InputText id="name" onChange={(e) => {this.updateProperty('name', e.target.value)}} value={this.state.dep.name}/>
                        </div>
                    </div>
                    <div className="ui-grid-row">
                        <div className="ui-grid-col-4" style={{padding:'4px 10px'}}><label htmlFor="Description">Description</label></div>
                        <div className="ui-grid-col-8" style={{padding:'4px 10px'}}>
                            <InputText id="description" onChange={(e) => {this.updateProperty('description', e.target.value)}} value={this.state.dep.description}/>
                        </div>
                    </div>
                    <div>
                        {this.newDep ? null :
                            <div>
                                <Button label="Show Employees" onClick={()=>this.listMyEmployees()}/>
                            </div>
                        }
                    </div>
                </div>}
            </Dialog>

            <Dialog visible={this.state.displayMyEmployeeDialog} modal={true} onHide={() => this.setState({displayMyEmployeeDialog: false})}>
                <DataTable value={this.state.employees} selectionMode="multiple" selection={this.state.selectedEmployees}
                           onSelectionChange={(e) => this.setState({selectedEmployees: e.data})}>
                    <Column field="name" header="Name"/>
                    <Column field="surname" header="Surame"/>
                </DataTable>
                <Button label="Remove Selected Employee" onClick={()=>this.removeEmployees()}/>
                <Button label="Add Employee" onClick={()=>this.listAllEmployees()}/>
            </Dialog>

            <Dialog visible={this.state.displayAllEmployeeDialog} modal={true} onHide={()=>this.setState({displayAllEmployeeDialog: false})} heigt={"100px"}>
                <MultiSelect value={this.state.newEmployees} options={this.allEmployees.map(e => ({label: e.name + " " + e.surname, value: e}))}
                             onChange={e => this.setState({newEmployees: e.value})}/>
                <Button label="Add" onClick={() => this.addEmployees()}/>
            </Dialog>

        </div>);
    }

}