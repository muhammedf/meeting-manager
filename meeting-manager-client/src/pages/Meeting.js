import React from "react"

import {DataTable} from "primereact/components/datatable/DataTable"
import {Column} from "primereact/components/column/Column"
import {Button} from "primereact/components/button/Button"
import {Dialog} from "primereact/components/dialog/Dialog"
import {InputText} from "primereact/components/inputtext/InputText"
import {MultiSelect} from 'primereact/components/multiselect/MultiSelect';

import {MetApi} from "../api/ApiObject"
import {DepApi} from "../api/ApiObject";

export default class Meeting extends React.Component{

    constructor() {
        super();
        this.state = {};
        this.save = this.save.bind(this);
        this.delete = this.delete.bind(this);
        this.onMetSelect = this.onMetSelect.bind(this);
        this.addNew = this.addNew.bind(this);

        this.allDepartments=[];
    }

    componentDidMount() {
        MetApi.listall().end((err, res) => this.setState({mets: res.body}));
    }

    save() {
        let mets = [...this.state.mets];
        let met = this.state.met
        if(this.newMet)
            MetApi.create(this.state.met).end((err, res) => {
                met.id=res.body;
                mets.push(met);
                this.setState({mets:mets});
            });
        else{
            mets[this.findSelectedMetIndex()] = this.state.met;
            MetApi.update(this.state.met).end();
        }

        this.setState({mets:mets, selectedMet:null, met: null, displayDialog:false});
    }

    delete() {
        let index = this.findSelectedMetIndex();
        MetApi.delete(this.state.selectedMet.id).end((err, res) =>{
            this.setState({
                mets: this.state.mets.filter((val,i) => i !== index),
                selectedMet: null,
                met: null,
                displayDialog: false});
        })
    }

    findSelectedMetIndex(){
        return this.state.mets.indexOf(this.state.selectedMet);
    }

    updateProperty(property, value) {
        let met = this.state.met;
        met[property] = value;
        this.setState({met: met});
    }

    onMetSelect(e){
        this.newMet = false;
        this.setState({
            displayDialog:true,
            met: Object.assign({}, e.data),
            departments: [],
        });
        this.allDepartmentsFetched=false;
        this.myDepartmentsFetched=false;
    }

    addNew() {
        this.newMet = true;
        this.setState({
            met: {name:'', description: ''},
            displayDialog: true
        });
    }

    listMyDepartments(){
        if(!this.myDepartmentsFetched){
            MetApi.listDepartments(this.state.met.id).end((err, res) => this.setState({departments: res.body, myDepartmentsFetched: true}));
        }
        this.setState({displayMyDepartmentDialog: true})
    }

    listAllDepartments(){
        if(!this.allDepartmentsFetched){
            DepApi.listall().end((err, res) => {
                var diff = res.body.filter(x => this.state.departments.every((elem, index, array) => elem.id !=x.id));
                window.diff=diff;
                this.allDepartments=diff;
                this.setState({displayAllDepartmentDialog: true});
            });
        }
    }

    addDepartments(){
        let departments=this.state.departments;
        if(!this.state.newDepartments) return;
        this.state.newDepartments.forEach( ne =>
            MetApi.addDepartment(this.state.met.id, ne.id).end((err, res) => {
                if(departments.every((elem, index, array) => elem.id != ne.id)) departments.push(ne);
                this.setState({departments: departments});
            })
        )
        this.setState({displayAllDepartmentDialog: false});
    }

    removeDepartments(){
        let departments=this.state.selectedDepartments;
        if(!departments) return;
        departments.forEach(e => MetApi.removeDepartment(this.state.met.id, e.id).end((err, res) =>{
            let i = this.state.departments.indexOf(e);
            this.setState({departments: this.state.departments.filter(dep => dep.id != e.id)});
        }))
    }

    render(){

        let header = <div className="ui-helper-clearfix" style={{lineHeight:'1.87em'}}>Meetings</div>;

        let footer = <div className="ui-helper-clearfix" style={{width:'100%'}}>
            <Button style={{float:'left'}} icon="fa-plus" label="Add" onClick={this.addNew}/>
        </div>;

        let dialogFooter = <div className="ui-dialog-buttonpane ui-helper-clearfix">
            <Button icon="fa-close" label="Delete" onClick={this.delete}/>
            <Button label="Save" icon="fa-check" onClick={this.save}/>
        </div>;

        return (<div className="content-section implementation">
            <DataTable value={this.state.mets} paginator={true} rows={15}  header={header} footer={footer}
                       selectionMode="single" selection={this.state.selectedMet} onSelectionChange={(e)=>{this.setState({selectedMet:e.data});}}
                       onRowSelect={this.onMetSelect}>
                <Column field="name" header="Name" sortable={true} />
                <Column field="description" header="Description" sortable={false} />
            </DataTable>

            <Dialog visible={this.state.displayDialog} header="Meeting Details" modal={true} footer={dialogFooter} onHide={() => this.setState({displayDialog: false})}>
                {this.state.met && <div className="ui-grid ui-grid-responsive ui-fluid">
                    <div className="ui-grid-row">
                        <div className="ui-grid-col-4" style={{padding:'4px 10px'}}><label htmlFor="name">Name</label></div>
                        <div className="ui-grid-col-8" style={{padding:'4px 10px'}}>
                            <InputText id="name" onChange={(e) => {this.updateProperty('name', e.target.value)}} value={this.state.met.name}/>
                        </div>
                    </div>
                    <div className="ui-grid-row">
                        <div className="ui-grid-col-4" style={{padding:'4px 10px'}}><label htmlFor="Description">Description</label></div>
                        <div className="ui-grid-col-8" style={{padding:'4px 10px'}}>
                            <InputText id="description" onChange={(e) => {this.updateProperty('description', e.target.value)}} value={this.state.met.description}/>
                        </div>
                    </div>
                    <div>
                        {this.newMet ? null :
                            <div>
                                <Button label="Show Departments" onClick={()=>this.listMyDepartments()}/>
                            </div>
                        }
                    </div>
                </div>}
            </Dialog>

            <Dialog visible={this.state.displayMyDepartmentDialog} modal={true} onHide={() => this.setState({displayMyDepartmentDialog: false})}
                    header="Participant Departments">
                <DataTable value={this.state.departments} selectionMode="multiple" selection={this.state.selectedDepartments}
                           onSelectionChange={(e) => this.setState({selectedDepartments: e.data})}>
                    <Column field="name" header="Name"/>
                    <Column field="description" header="Description"/>
                </DataTable>
                <Button label="Remove Selected Department" onClick={()=>this.removeDepartments()}/>
                <Button label="Add Department" onClick={()=>this.listAllDepartments()}/>
            </Dialog>

            <Dialog visible={this.state.displayAllDepartmentDialog} modal={true} onHide={()=>this.setState({displayAllDepartmentDialog: false})}
                    heigt={"100px"} header="Invite Other Departments">
                <div className="ui-grid-row ui-fluid" style={({minHeight: "100px"})}>
                    <div className="ui-grid-col-6">
                        <MultiSelect value={this.state.newDepartments} options={this.allDepartments.map(e => ({label: e.name, value: e}))}
                             onChange={e => this.setState({newDepartments: e.value})}/>
                    </div>
                    <div className="ui-grid-col-6">
                        <Button label="Add" onClick={() => this.addDepartments()}/>
                    </div>
                </div>
            </Dialog>

        </div>);
    }

}