import React from "react"

import {DataTable} from "primereact/components/datatable/DataTable"
import {Column} from "primereact/components/column/Column"
import {Button} from "primereact/components/button/Button"
import {Dialog} from "primereact/components/dialog/Dialog"
import {InputText} from "primereact/components/inputtext/InputText"

import {DepApi} from "../api/ApiObject"

export default class Department extends React.Component{

    constructor() {
        super();
        this.state = {};
        this.save = this.save.bind(this);
        this.delete = this.delete.bind(this);
        this.onDepSelect = this.onDepSelect.bind(this);
        this.addNew = this.addNew.bind(this);
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
            dep: Object.assign({}, e.data)
        });
    }

    addNew() {
        this.newDep = true;
        this.setState({
            dep: {name:'', description: ''},
            displayDialog: true
        });
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
                    <div className="ui-grid-row">
                    </div>
                </div>}
            </Dialog>
        </div>);
    }

}