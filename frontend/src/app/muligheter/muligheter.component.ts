import { Component, OnInit } from '@angular/core';

import { environment } from '@env/environment';

@Component({
  selector: 'app-muligheter',
  templateUrl: './muligheter.component.html',
  styleUrls: ['./muligheter.component.scss']
})
export class MuligheterComponent implements OnInit {

  version: string = environment.version;

  constructor() { }

  ngOnInit() {
  }

}
