import request from "superagent"

class CRUDApi {

    constructor(url){
        this.url = url;
    }

    findById(id){
        return request.get(this.url+"/"+id);
    }

    create(entity){
        return request.post(this.url+"/")
            .set('Accept', 'application/json').send(entity);
    }

    update(entity){
        return request.put(this.url+"/"+entity.id, entity);
    }

    delete(id){
        return request.delete(this.url+"/"+id);
    }

    listall(){
        return request.get(this.url+"/");
    }

}

export class EmployeeApi extends CRUDApi{

    constructor(baseurl){
        super(baseurl+"/employee");
    }

}

export class DepartmentApi extends CRUDApi{

    constructor(baseurl){
        super(baseurl+"/department");
    }

    listEmployees(id){
        return request.get(this.url+"/"+id+"/employee/");
    }

    addEmployee(did, eid){
        return request.put(this.url+"/"+did+"/employee/"+eid);
    }

    removeEmployee(did, eid){
        return request.delete(this.url+"/"+did+"/employee/"+eid);
    }

}

export class MeetingApi extends CRUDApi{

    constructor(baseurl){
        super(baseurl+"/meeting");
    }

    listDepartments(id){
        return request.get(this.url+"/"+id+"/department/");
    }

    addDepartment(mid, did){
        return request.put(this.url+"/"+mid+"/department/"+did);
    }

    removeDepartment(mid, did){
        return request.delete(this.url+"/"+mid+"/employee/"+did);
    }

}
