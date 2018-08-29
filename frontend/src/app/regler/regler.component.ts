import { Component, OnInit } from '@angular/core';

import { environment } from '@env/environment';

@Component({
  selector: 'app-regler',
  templateUrl: './regler.component.html',
  styleUrls: ['./regler.component.scss']
})
export class ReglerComponent implements OnInit {

  version: string = environment.version;

  constructor() { }

  ngOnInit() { }

}
