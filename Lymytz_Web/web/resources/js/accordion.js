/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(function() {
    var accordions = document.getElementsByClassName("accordion");
    if (accordions) {
        for (var i = 0; i < accordions.length; i++) {
            var indexActive = 0;
            var accordion = accordions[i];
            accordion.setAttribute("index-active", indexActive);
            for (var index = 0; index < accordion.children.length; index++) {
                var element = accordion.children[index];
                var mode = element.getAttribute("data-mode");
                if (mode ? mode !== "simple" : true) {
                    element.setAttribute("index", index);
                    element.setAttribute("aria-expanded", index === indexActive);
                    if (index !== indexActive) {
                        slideUp(element);
                    }
                    var header = element.getElementsByTagName("h3")[0];
                    header.addEventListener("click", function(event) {
                        event.preventDefault();
                        var parent = this.parentElement;
                        var accordion = parent.parentElement;
                        var expanded = parent.getAttribute("aria-expanded");
                        var indexActive = parent.getAttribute("index");
                        slide(parent, expanded);
                        if (expanded === "false") {
                            for (var i = 0; i < accordion.children.length; i++) {
                                if (i !== Number(indexActive)) {
                                    slideUp(accordion.children[i]);
                                }
                            }
                        }
                        accordion.setAttribute("index-active", indexActive);
                    });
                }
            }
        }
    }
});

function slide(element, expanded) {
    $(element.getElementsByClassName("menu")).slideToggle();
    element.setAttribute("aria-expanded", expanded === "true" ? "false" : "true");
}

function slideUp(element) {
    $(element.getElementsByClassName("menu")).slideUp();
    element.setAttribute("aria-expanded", "false");
}

function slideDown(element) {
    $(element.getElementsByClassName("menu")).slideDown();
    element.setAttribute("aria-expanded", "true");
}

function getModuleUrlIndex() {
    var indexActive = 0;
    var url = decodeURI(document.location.href);
    if (url.includes("/Données De Base/")) {
        indexActive = 0;
    } else if (url.includes("/Commercial/")) {
        indexActive = 1;
    } else if (url.includes("/Production/")) {
        indexActive = 2;
    } else if (url.includes("/Comptabilité/")) {
        indexActive = 3;
    } else if (url.includes("/Ressources Humaine/")) {
        indexActive = 4;
    } else if (url.includes("/gescrm/")) {
        indexActive = 5;
    } else if (url.includes("/Mutuelle/")) {
        indexActive = 6;
    } else if (url.includes("/Gestion Projet/")) {
        indexActive = 7;
    } else if (url.includes("/Paramètrages/")) {
        indexActive = 8;
    } else if (url.includes("/Statistiques/")) {
        indexActive = 9;
    }
    var menu = document.getElementById("menu-navigation");
    if (menu) {
        menu.setAttribute("index-active", indexActive);
        for (var i = 0; i < menu.children.length; i++) {
            if (i === indexActive) {
                slideDown(menu.children[i]);
            } else {
                slideUp(menu.children[i]);
            }
        }
    }
}



