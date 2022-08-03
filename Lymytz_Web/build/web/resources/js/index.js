/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(function() {
//    showMacAddress();
});

function showMacAddress() {
    var macAddress = "";
    var user_address_ip = "";
    var your_computernm = "";
    var wmi = GetObject("winmgmts:{impersonationLevel=impersonate}");
    e = new Enumerator(wmi.ExecQuery("SELECT * FROM Win32_NetworkAdapterConfiguration WHERE IPEnabled = True"));
    for (; !e.atEnd(); e.moveNext()) {
        var s = e.item();
        macAddress = s.MACAddress;
//        console.log(macAddress);
        user_address_ip = s.IPAddress(0);
//        console.log(user_address_ip);
        your_computernm = s.DNSHostName;
//        console.log(your_computernm);
    }
}

function showMacAddress_OLD() {
    var obj = new ActiveXObject("WbemScripting.SWbemLocator");
    var s = obj.ConnectServer("MACHINE");
//    var s = obj.ConnectServer(".");
    var properties = s.ExecQuery("SELECT * FROM Win32_NetworkAdapterConfiguration");
    var e = new Enumerator(properties);

    var output;
    output = '<table border="0" cellpadding="5px" cellspacing="1px" bgcolor="#CCCCCC">';
    output = output + '<tr bgcolor="#EAEAEA"><td>Caption</td><td>MACAddress</td></tr>';
    while (!e.atEnd())
    {
        e.moveNext();
        var p = e.item();
        if (!p)
            continue;
        output = output + '<tr bgcolor="#FFFFFF">';
        output = output + '<td>' + p.Caption;
        +'</td>';
        output = output + '<td>' + p.MACAddress + '</td>';
        output = output + '</tr>';
    }
    output = output + '</table>';
    console.log(output);
    document.getElementById("txtMACAdress").innerHTML = output;
}

