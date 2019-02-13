import { Component, OnInit } from '@angular/core';
//import * as $ from 'jquery';
@Component({
  selector: 'app-tab',
  templateUrl: './tab.component.html',
  styleUrls: ['./tab.component.css']
})
export class TabComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }
//   valida() {

//     $(".sidebar-dropdown > a").click(function() {
//   $(".sidebar-submenu").slideUp(600);
//   if (
//     $(this)
//       .parent()
//       .hasClass("active")
//   ) {
//     $(".sidebar-dropdown").removeClass("active");
//     $(this)
//       .parent()
//       .removeClass("active");
//   } else {
//     $(".sidebar-dropdown").removeClass("active");
//     $(this)
//       .next(".sidebar-submenu")
//       .slideDown(200);
//     $(this)
//       .parent()
//       .addClass("active");
//   }
// });

// $("#close-sidebar").click(function() {
//   $(".page-wrapper").removeClass("toggled");
// });
// $("#show-sidebar").click(function() {
//   $(".page-wrapper").addClass("toggled");
// });
// }

}
