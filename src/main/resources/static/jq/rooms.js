 $(document).ready(function(){

    var roomName = [[${roomName}]];

    if(roomName != null)
    $("button[name='btn-create']").on("click", function (e){
    e.preventDefault();

    var name = $("input[name='name']").val();
    console.log("-----방제목:" + name);
    if(name == "") {
        alert("방제목을 입력하세요")
    }else {
        $("form").submit();
        alert("'" + roomName.name + "' 방이 개설되었습니다.");
    }
});



});