import React from "react"

import {DataTable} from "primereact/components/datatable/DataTable"
import {Column} from "primereact/components/column/Column"
import {Button} from "primereact/components/button/Button"
import {Dialog} from "primereact/components/dialog/Dialog"
import {InputText} from "primereact/components/inputtext/InputText"

import {EmpApi} from "../api/ApiObject";

export default class Employee extends React.Component{

    constructor() {
        super();
        this.state = {};
        this.save = this.save.bind(this);
        this.delete = this.delete.bind(this);
        this.onEmpSelect = this.onEmpSelect.bind(this);
        this.addNew = this.addNew.bind(this);
    }

    componentDidMount() {
        EmpApi.listall().end((err, res) => this.setState({emps: res.body}));
    }

    save() {
        let emps = [...this.state.emps];
        let emp = this.state.emp
        if(this.newEmp)
            EmpApi.create(this.state.emp).end((err, res) => {
                emp.id=res.body;
                emps.push(emp);
                this.setState({emps:emps});
            });
        else{
            emps[this.findSelectedEmpIndex()] = this.state.emp;
            EmpApi.update(this.state.emp).end();
        }

        this.setState({emps:emps, selectedEmp:null, emp: null, displayDialog:false});
    }

    delete() {
        let index = this.findSelectedEmpIndex();
        EmpApi.delete(this.state.selectedEmp.id).end((err, res) =>{
            this.setState({
                emps: this.state.emps.filter((val,i) => i !== index),
                selectedEmp: null,
                emp: null,
                displayDialog: false});
        })
    }

    findSelectedEmpIndex(){
        return this.state.emps.indexOf(this.state.selectedEmp);
    }

    updateProperty(property, value) {
        let emp = this.state.emp;
        emp[property] = value;
        this.setState({emp: emp});
    }

    onEmpSelect(e){
        this.newEmp = false;
        this.setState({
            displayDialog:true,
            emp: Object.assign({}, e.data)
        });
    }

    addNew() {
        this.newEmp = true;
        this.setState({
            emp: {name:'', surname: '', salary: ''},
            displayDialog: true
        });
    }

    render(){

        let header = <div className="ui-helper-clearfix" style={{lineHeight:'1.87em'}}>Employees</div>;

        let footer = <div className="ui-helper-clearfix" style={{width:'100%'}}>
            <Button style={{float:'left'}} icon="fa-plus" label="Add" onClick={this.addNew}/>
        </div>;

        let dialogFooter = <div className="ui-dialog-buttonpane ui-helper-clearfix">
            <Button icon="fa-close" label="Delete" onClick={this.delete}/>
            <Button label="Save" icon="fa-check" onClick={this.save}/>
        </div>;

        return (<div className="content-section implementation">
            <DataTable value={this.state.emps} paginator={true} rows={15}  header={header} footer={footer}
                       selectionMode="single" selection={this.state.selectedEmp} onSelectionChange={(e)=>{this.setState({selectedEmp:e.data});}}
                       onRowSelect={this.onEmpSelect}>
                <Column field="name" header="Name" sortable={true} />
                <Column field="surname" header="Surname" sortable={true} />
                <Column field="salary" header="Salary" sortable={true} />
            </DataTable>

            <Dialog visible={this.state.displayDialog} header="Emp Details" modal={true} footer={dialogFooter} onHide={() => this.setState({displayDialog: false})}>
                {this.state.emp && <div className="ui-grid ui-grid-responsive ui-fluid">
                    <div className="ui-grid-row">
                        <div className="ui-grid-col-4" style={{padding:'4px 10px'}}><label htmlFor="name">Name</label></div>
                        <div className="ui-grid-col-8" style={{padding:'4px 10px'}}>
                            <InputText id="name" onChange={(e) => {this.updateProperty('name', e.target.value)}} value={this.state.emp.name}/>
                        </div>
                    </div>
                    <div className="ui-grid-row">
                        <div className="ui-grid-col-4" style={{padding:'4px 10px'}}><label htmlFor="surname">Surname</label></div>
                        <div className="ui-grid-col-8" style={{padding:'4px 10px'}}>
                            <InputText id="surname" onChange={(e) => {this.updateProperty('surname', e.target.value)}} value={this.state.emp.surname}/>
                        </div>
                    </div>
                    <div className="ui-grid-row">
                        <div className="ui-grid-col-4" style={{padding:'4px 10px'}}><label htmlFor="salary">Salary</label></div>
                        <div className="ui-grid-col-8" style={{padding:'4px 10px'}}>
                            <InputText id="salary" onChange={(e) => {this.updateProperty('salary', e.target.value)}} value={this.state.emp.salary}/>
                        </div>
                    </div>
                </div>}
            </Dialog>
        </div>);
    }
}