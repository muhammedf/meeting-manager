import request from "superagent"

class CRUDApi {

    constructor(url){
        this.url = url;
    }

    findById(id){
        return request.get(this.url+"/"+id);
    }

    create(entity){
        return request.post(this.url+"/") .set('X-API-Key', 'foobar')
            .set('Accept', 'application/json').send(entity);
    }

    update(entity){
        return request.put(this.url+"/"+entity.id, entity);
    }

    delete(id){
        return request.delete(this.url+"/"+id);
    }

    listall(){
        console.log(this.url);
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

}

export class MeetingApi extends CRUDApi{

    constructor(baseurl){
        super(baseurl+"/meeting");
    }

}
