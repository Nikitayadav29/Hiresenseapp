package hiresenseapp.pojo;

public class ResumeAnalysisLogPojo {
private int id;
private String userId;
private String jsonResult;
private String createdAt;
public ResumeAnalysisLogPojo(int id, String userId, String jsonResult, String createdAt) {
	super();
	this.id = id;
	this.userId = userId;
	this.jsonResult = jsonResult;
	this.createdAt = createdAt;
}
public ResumeAnalysisLogPojo() {
	
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getUserId() {
	return userId;
}
public void setUserId(String userId) {
	this.userId = userId;
}
public String getJsonResult() {
	return jsonResult;
}
public void setJsonResult(String jsonResult) {
	this.jsonResult = jsonResult;
}
public String getCreatedAt() {
	return createdAt;
}
public void setCreatedAt(String createdAt) {
	this.createdAt = createdAt;
}
@Override
public String toString() {
    return "ResumeAnalysisLogPojo{" + "id=" + id + ", userId=" + userId + '}';
}

}
