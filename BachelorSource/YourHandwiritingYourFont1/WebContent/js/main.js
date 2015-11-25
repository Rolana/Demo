/**
 * main.js
 *
 * responsible for adding menu to the page
 */



;(function ( window, document, Btn, FastClick, undefined) {
    'use strict';



    // Used for creating alert messages popups for button
    function alertEvent( title ) {
        return function() { window.alert( title ); };
    }



    // Used for creating links for button
    function linkEvent() {
        return function() { 
			Replace = 0;
			var clicked = this.name;
			
		
			ChangeImage(clicked);

			for (var l=0;l<items.length;l++)
			{
				if(items[l][0] == clicked)
					{
					var x = confirm("Replace '" + clicked + "' Coordinates");
					if(x == false)
						{
						alert("canceled");
						break;
						}
					items[l][1] = $('#x1').val();
					items[l][2] = $('#y1').val();
					items[l][3] = $('#w').val();
					items[l][4] = $('#h').val();
					//items[l][5] = $('#w').val();
					//items[l][6] = $('#h').val();
					Replace = 1;
					alert("replaced");
					}
			}
    };
    }



    // remove CSS that makes the menu invisible onload
    function removeIsMainInvisble() {
        document.documentElement.classList.remove('is-main-invisible');
    }



    // The functions that run after `DOMContetedLoaded`
    function onload() {
        // fast click for iOS / Android
        if ( document.documentElement.classList.contains('touch-fix') ) FastClick.attach(document.body);


        // creates the menu
        new Btn( 'Menu' ).addClass('skin-main_menu')
            .append( new Btn('A').addClass('skin-www').on( 'click', linkEvent() ) )
/*
            .append( new Btn('orilivni.com').addClass('skin-blog').on( 'click', linkEvent() ) )
*/
            .append( new Btn('A').addClass('skin-www').on( 'click', linkEvent() ) )

/*            .append( new Btn('Sub Menu').addClass('skin-menu')

                .append( new Btn('Disaster Artist').addClass('skin-disaster_artist').on( 'click', linkEvent() ) )

                .append( new Btn('Nyan Cat').addClass('skin-nyan_cat').on( 'click', linkEvent() ) )

                .append( new Btn('Streets of Rage').addClass('skin-streets_of_rage').on( 'click', linkEvent() ) )

                .append( new Btn('Sub Sub Menu').addClass('skin-menu')
                    .append( new Btn('Alert: 1!').addClass('skin-alert').on( 'click', alertEvent('Annoying alert 1!') ) )
                    .append( new Btn('Alert: 2!').addClass('skin-alert').on( 'click', alertEvent('Annoying alert 2!') ) )
                ) )

            .append( new Btn('Hacker News').addClass('skin-hacker_news').on( 'click', linkEvent() ) )

            .append( new Btn('Fontef').addClass('skin-fontef').on( 'click', linkEvent() ) )

            .append( new Btn('Processing').addClass('skin-processing').on( 'click', linkEvent() ) )

            .append( new Btn('HTML5 Rocks!').addClass('skin-html5rocks').on( 'click', linkEvent() ) )
*/
            // Appending the button menu to the DOM - `#main` element
            .appendTo( '#buttonsmenu' );



        // remove the class that makes the menu invisible
       // if ('requestAnimationFrame' in window) {
         //   window.requestAnimationFrame( removeIsMainInvisble );
        //} else if ('webkitRequestAnimationFrame' in window) {
          //  window.webkitRequestAnimationFrame( removeIsMainInvisble );
        //} else {
         //   window.setTimeout( removeIsMainInvisble, 0 );
        //}
    }



    // the init function - also call it self
    (function init() {
        // iOS / Android - touch hack fix by sniff user agent - maybe better to use modernizr touch event detect - but not sure it's a problem of all Webkit
        document.documentElement.className += ((/(like Mac OS X)|(Android)/i.test(window.navigator.userAgent)) ? ' touch-fix' : ' no-touch-fix');

        // Makes the menu invisible for fading in animation
        document.documentElement.className += ' is-main-invisible';

        // Makes sure everything is work when blocking render
        document.addEventListener( 'DOMContentLoaded', onload, false );
    } ());


} (window, document, Btn, FastClick) );