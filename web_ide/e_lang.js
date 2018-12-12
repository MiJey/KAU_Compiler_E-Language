var txt ="";
var last = "";
window.onload = function(){
  var a = document.getElementsByClassName("btn");
  for(var i=0;i<a.length;i++){
    a[i].onclick=btnClicked;
  }
  document.onkeydown = keydown;
}
function readJSONResponse(response) {
  var responseData = '';
  response.on('data', function (chunk) {
    responseData += chunk;
  });
  response.on('end', function () {
    var dataObj = JSON.parse(responseData);
    console.log("Raw Response: " +responseData);
    console.log("Message: " + dataObj.message);
    console.log("Question: " + dataObj.question);
  });
}
function sendAjax(url, data){

  // 입력값을 변수에 담고 문자열 형태로 변환
  var data = txt;

  // content-type을 설정하고 데이터 송신
  var xhr = new XMLHttpRequest();
  xhr.open('POST', url, true);
  xhr.setRequestHeader('Content-type', "text/plain");
  xhr.send(data);

  // 데이터 수신이 완료되면 표시

  xhr.addEventListener('load', function(){
    // 문자열 형식으로 변환
    var result = xhr.responseText;
    document.getElementById("console").innerHTML = result;
  });
}

function btnClicked(){
  if(this.id == "startbtn"){
    download('a.txt',txt);
    //getConsole();
/*
    var form = document.createElement("form");
    form.setAttribute("method", "post");
    form.setAttribute("action", "http://127.0.0.1:8080");

    var hiddenField = document.createElement("input");
    hiddenField.setAttribute("type", "hidden");
    hiddenField.setAttribute("name", "data");
    hiddenField.setAttribute("value", txt);
    form.appendChild(hiddenField);
    document.body.appendChild(form);
    form.submit();


  var http = require('http');
  var options = {
    host: '127.0.0.1',
    path: '/',
    port: '8080',
    method: 'POST'
  };
  var req = http.request(options, readJSONResponse);
  req.write(txt);
  req.end();

  var req = new XMLHttpRequest();
  req.open("POST", "http://127.0.0.1:8080/", true);
  req.setRequestHeader('Content-type', "text/plain");
  req.addEventListener('load', function(){
    // 문자열 형식으로 변환
    var result = req.responseText;
    document.getElementById("console").innerHTML = result;
  });
  req.send();*/
  //sendAjax('http://127.0.0.1:8080/', txt);

  }else if(this.id=="<-"){
    pushBack();
  }else if(this.id=="space"){
    txt+=" ";
    document.getElementById("text").innerHTML+="&nbsp;";
    last = "space";
  }else if(this.id =="tab"){
    txt+="    ";
    document.getElementById("text").innerHTML+="&nbsp;&nbsp;&nbsp;&nbsp;";
    last = "tab";
  }else if(this.id=="enter"){
    txt+="\n";
    document.getElementById("text").innerHTML+="<br>";
    last = "enter";
  }else{
    txt += this.id;
    //txt+=this.value;//"\\u"+this.id.charCodeAt(0).toString(21);
    document.getElementById("text").innerHTML+=this.id;
    last = "";
  }
}
function keydown(e) {
    e = e || window.event;
    var num = parseInt(e.keyCode)
    if(num == 8){
      pushBack();
    }else if(num == 9 || num == 13 || num == 32){
      var input = document.getElementsByName(e.keyCode)[0];
      if(input.id =='tab'){
        txt+="    ";
        document.getElementById("text").innerHTML+="&nbsp;&nbsp;&nbsp;&nbsp;";
        last = "tab";
      }else if(input.id =='space'){
        txt+=" ";
        document.getElementById("text").innerHTML+="&nbsp;";
        last = "space";
      }else if(input.id =='enter'){
        txt+="\n";
        document.getElementById("text").innerHTML+="<br>";
        last = "enter";
      }
    }
    else{
      var input = document.getElementsByName(e.keyCode)[0];
      document.getElementById("text").innerHTML+=input.id;
      //txt+=input.value;//"\\u"+this.id.charCodeAt(0).toString(21);
      txt += input.id;
      last = "";
    }
}

function pushBack(){
  var temp = document.getElementById("text").innerHTML;
  if(last == 'tab'){
    txt = txt.substring(0,txt.length-4);
    temp = temp.substring(0,temp.length-24);
  }else if(last == 'enter'){
    txt = txt.substring(0,txt.length-1);
    temp = temp.substring(0,temp.length-2);
  }else if(last == 'space'){
    txt = txt.substring(0,txt.length-1);
    temp = temp.substring(0,temp.length-6);
  }else{
    txt = txt.substring(0,txt.length-2);
    temp = temp.substring(0,temp.length-2);
  }
  document.getElementById("text").innerHTML = temp;
  last = "";
}
function download(filename, text) {
    var pom = document.createElement('a');
    pom.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(text));
    pom.setAttribute('download', filename);

    if (document.createEvent) {
        var event = document.createEvent('MouseEvents');
        event.initEvent('click', true, true);
        pom.dispatchEvent(event);
    }
    else {
        pom.click();
        alert("download error");
    }
}

function post_to_url(path, params, method) {
    method = method || "post"; // Set method to post by default, if not specified.
    // The rest of this code assumes you are not using a library.
    // It can be made less wordy if you use one.
    var form = document.createElement("form");
    form.setAttribute("method", method);
    form.setAttribute("action", path);

    for(var key in params) {
        var hiddenField = document.createElement("input");
        hiddenField.setAttribute("type", "hidden");
        hiddenField.setAttribute("name", key);
        hiddenField.setAttribute("value", params[key]);
        form.appendChild(hiddenField);
    }
    document.body.appendChild(form);
    form.submit();
}

function getConsole() {
  new Ajax.Request("b.txt", {
    method: "get",
    onSuccess: printConsole,
    onFailure: ajaxFailure
  });
}
function ajaxFailure(ajax, exception) {
  alert("Error making Ajax request:" +"\n\nServer status:\n" + ajax.status + " " + ajax.statusText +"\n\nServer response text:\n" + ajax.responseText);
  if (exception) {
    throw exception;
  }
}
function printConsole(ajax){
  var temp = ajax.responseText.strip().split('\n');
  var console = "";
  temp.forEach(function(element) {
    console += element+"<br>";
  });
  document.getElementById("console").innerHTML = console;
}
