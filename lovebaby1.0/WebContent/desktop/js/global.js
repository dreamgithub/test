//����desktop�ռ�,��װ��ز���
myLib.NS("desktop");
myLib.desktop={
	winWH:function(){
		$('body').data('winWh',{'w':$(window).width(),'h':$(window).height()});
 	},
	desktopPanel:function(){
		$('body').data('panel',{
					   'taskBar':{
					   '_this':$('#taskBar'),
					   'task_lb':$('#task_lb')
										},
					   'lrBar':{
					   '_this':$('#lr_bar'),	   
					   'default_app':$('#default_app'),
				       'start_block':$('#start_block'),
				       'start_btn':$('#start_btn'),
					   'start_item':$('#start_item'),
					   'default_tools':$('#default_tools')
							},				
						'deskIcon':{
							'_this':$('#deskIcon'),
							'icon':$('li.desktop_icon')
							},
						'powered_by':$('a.powered_by')
						});
		},
	getMydata:function(){
		return $('body').data();
		},
	mouseXY:function(){
		var mouseXY=[];
		$(document).bind('mousemove',function(e){ 
			mouseXY[0]=e.pageX;
			mouseXY[1]=e.pageY;
           });
		return mouseXY;
		},	
	contextMenu:function(jqElem,data,menuName,textLimit){
		  var _this=this
		      ,mXY=_this.mouseXY();
		  
          jqElem
		  .smartMenu(data,{
            name: menuName,
			textLimit:textLimit,
			afterShow:function(){
				var menu=$("#smartMenu_"+menuName);
				var myData=myLib.desktop.getMydata(),
		            wh=myData.winWh;//��ȡ��ǰdocument���
 				var menuXY=menu.offset(),menuH=menu.height(),menuW=menu.width();
				if(menuXY.top>wh['h']-menuH){
					menu.css('top',mXY[1]-menuH-2);
					}
				if(menuXY.left>wh['w']-menuW){
					menu.css('left',mXY[0]-menuW-2);
					}	
				}
           });
		  $(document.body).click(function(event){
			event.preventDefault(); 			  
			$.smartMenu.hide();
						  });
		}	
	}
	
//������ز���
myLib.NS("desktop.win");
myLib.desktop.win={
	winHtml:function(title,url,id){
		return "<div class='windows corner rgba' id="
		+id
		+"><div class='win_title'><b>"
		+title
		+"</b><span class='win_btnblock'><a href='#' class='winMinimize' title='��С��'></a><a href='#' class='winMaximize' title='���'></a><a href='#' class='winHyimize' title='��ԭ'></a><a href='#' class='winClose' title='�ر�'></a></span></div><iframe frameborder='0' name='myFrame_"
		+id
		+"' id='myFrame_"
		+id
		+"' class='winframe' scrolling='auto' width='100%' src='"
		+url
		+"'></iframe></div>";
		},
	 //������ϲ㣬�޸�iframe ��꾭���¼�bug	
	iframFix:function(obj){
		obj.each(function(){
		var o=$(this);								
		if(o.find('.zzDiv').size()<=0)
		o.append($("<div class='zzDiv' style='width:100%;height:"+(o.innerHeight()-26)+"px;position:absolute;z-index:900;left:0;top:26px;'></div>"));
				 })
		},	
	//��ȡ��ǰ��������z-indexֵ
	maxWinZindex:function($win){
		   return Math.max.apply(null, $.map($win, function (e, n) {
           if ($(e).css('position') == 'absolute')
            return parseInt($(e).css('z-index')) || 1;
              }));
			},
	findTopWin:function($win,maxZ){
		var topWin;
		$win.each(function(index){
 						   if($(this).css("z-index")==maxZ){
							   topWin=$(this);
							   return false;
							   } 
 						   });
		return topWin;  
		},		
	//�رմ���	
	closeWin:function(obj){
		var _this=this,$win=$('div.windows').not(":hidden"),maxZ,topWin;
 		myLib.desktop.taskBar.delWinTab(obj);
		obj.hide('slow',function(){
			$(this).remove();
				         });
		//���رմ��ں�Ѱ�����z-index�Ĵ��ڲ�ʹ�����ѡ��״̬
		if($win.size()>1){
		maxZ=_this.maxWinZindex($win.not(obj));
		topWin=_this.findTopWin($win,maxZ);
		_this.switchZindex(topWin);
		}
		},
	minimize:function(obj){
		var _this=this,$win=$('div.windows').not(":hidden"),maxZ,topWin,objTab;
		obj.hide();
		//��С�����ں�Ѱ�����z-index��������
		if($win.size()>1){
		maxZ=_this.maxWinZindex($win.not(obj));
		topWin=_this.findTopWin($win,maxZ);
		_this.switchZindex(topWin);
		}else{
			objTab=myLib.desktop.taskBar.findWinTab(obj);
			objTab.removeClass('selectTab').addClass('defaultTab');
			}
		},	
	//��󻯴��ں���	
	maximizeWin:function(obj){
		var myData=myLib.desktop.getMydata(),
		    wh=myData.winWh;//��ȡ��ǰdocument���
		obj
		.css({'width':wh['w'],'height':wh['h']-35,'left':0,'top':0})
		.draggable( "disable" )
		.resizable( "disable" )
		.fadeTo("fast",1)
		.find(".winframe")
		.css({'width':wh['w']-6,'height':wh['h']-64});
		},
	//��ԭ���ں���	
	hyimizeWin:function(obj){
		var myData=obj.data(),
		    winLocation=myData.winLocation;//��ȡ�������ǰ��λ�ô�С
			
		obj.css({'width':winLocation['w'],'height':winLocation['h'],'left':winLocation['left'],'top':winLocation['top']})
		.draggable( "enable" )
		.resizable( "enable" ) 
		.find(".winframe")
		.css({'width':winLocation['w']-6,'height':winLocation['h']-29});
		},	
	//��������z-indexֵ		
	switchZindex:function(obj){
		var myData=myLib.desktop.getMydata()
		    ,$topWin=myData.topWin
			,$topWinTab=myData.topWinTab
		    ,curWinZindex=obj.css("z-index")
			,maxZ=myData.maxZindex
			,objTab=myLib.desktop.taskBar.findWinTab(obj);
			
		if(!$topWin.is(obj)){
			
		obj.css("z-index",maxZ);
		objTab.removeClass('defaultTab').addClass('selectTab');
		
		$topWin.css("z-index",curWinZindex);
		$topWinTab.removeClass('selectTab').addClass('defaultTab');
		this.iframFix($topWin);
		//������㴰�ڶ���
		$('body').data("topWin",obj).data("topWinTab",objTab);
		}
		},
	//�½�����ʵ��	
    newWin:function(options){
		var _this=this;
		
		var myData=myLib.desktop.getMydata(),
		    wh=myData.winWh,//��ȡ��ǰdocument���
			$windows=$("div.windows"),
			curwinNum=myLib._is(myData.winNum,"Number")?myData.winNum:0;//�жϵ�ǰ���ж��ٴ���
			_this.iframFix($windows);
		//Ĭ�ϲ�������
        var defaults = {
            WindowTitle:          null,
			WindowsId:            null,
            WindowPositionTop:    'center',                          /* Posible are pixels or 'center' */
            WindowPositionLeft:   'center',                          /* Posible are pixels or 'center' */
            WindowWidth:          Math.round(wh['w']*0.6),           /* Only pixels */
            WindowHeight:         Math.round(wh['h']*0.8),           /* Only pixels */
            WindowMinWidth:       250,                               /* Only pixels */
            WindowMinHeight:      250,                               /* Only pixels */
		    iframSrc:             null,                              /* ��ܵ�src·��*/ 
            WindowResizable:      true,                              /* true, false*/
            WindowMaximize:       true,                              /* true, false*/
            WindowMinimize:       true,                              /* true, false*/
            WindowClosable:       true,                              /* true, false*/
            WindowDraggable:      true,                              /* true, false*/
            WindowStatus:         'regular',                         /* 'regular', 'maximized', 'minimized' */
            WindowAnimationSpeed: 500,
            WindowAnimation:      'none'
        };
		  
		var options = $.extend(defaults, options);
 		 
		//�жϴ���λ�ã�����ʹ��Ĭ��ֵ
		var wLeft=myLib._is(options['WindowPositionLeft'],"Number")?options['WindowPositionLeft']:(wh['w']-options['WindowWidth'])/2;
		var wTop=myLib._is(options['WindowPositionTop'],"Number")?options['WindowPositionTop']:(wh['h']-options['WindowHeight'])/2;
 		 
		//�����ڸ����µ�z-indexֵ
		var zindex=curwinNum+100;
		var id="myWin_"+options['WindowsId'];//���ݴ�����id����Ϊ�´���id
 		$('body').data("winNum",curwinNum+1);//���´�������
		
		//�ж������id�Ĵ��ڴ��ڣ��򲻴�������
		if($("#"+id).size()<=0){
			//�������������tab
			myLib.desktop.taskBar.addWinTab(options['WindowTitle'],options['WindowsId']);
			//��ʼ���´��ڲ���ʾ
			$("body").append($(_this.winHtml(options['WindowTitle'],options['iframSrc'],id)));
		
		var $newWin=$("#"+id)
		   ,$icon=$("#"+options['WindowsId'])
		   ,$iconOffset=$icon.offset()
		   ,$fram=$newWin.children(".winframe")
		   ,winMaximize_btn=$newWin.find('a.winMaximize')//��󻯰�ť
		   ,winMinimize_btn=$newWin.find('a.winMinimize')//��С����ť
		   ,winClose_btn=$newWin.find('a.winClose')//�رհ�ť
		   ,winHyimize_btn=$newWin.find('a.winHyimize');//��ԭ��ť
		   
		   winHyimize_btn.hide();
		   if(!options['WindowMaximize']) winMaximize_btn.hide();
		   if(!options['WindowMinimize']) winMinimize_btn.hide();
		   if(!options['WindowClosable']) winClose_btn.hide();
		
		//�洢��������z-indexֵ,����㴰�ڶ���
		$('body').data({"maxZindex":zindex,"topWin":$newWin});
		
		//�жϴ����Ƿ����ö���Ч��
		if(options.WindowAnimation=='none'){
			
		 $newWin
		 .css({"width":options['WindowWidth'],"height":options['WindowHeight'],"left":wLeft,"top":wTop,"z-index":zindex})
		 .addClass("loading")
		 .show();
		 
			}else{
	 		
		 $newWin
		 .css({"left":$iconOffset.left,"top":$iconOffset.top,"z-index":zindex})
		 .addClass("loading")
		 .show()
		 .animate({ 
            width: options['WindowWidth'], 
            height:options['WindowHeight'],
            top: wTop, 
            left: wLeft}, 500);
		 
				}
				
        $newWin
		.data('winLocation',{
			  'w':options['WindowWidth'],
			  'h':options['WindowHeight'],
			  'left':wLeft,
			  'top':wTop
			  })
		.find(".winframe")
		.css({"width":options['WindowWidth']-6,"height":options['WindowHeight']-29})
		
		//�ȴ�iframe�������
		//.load(function(){
					   
 		   //���ô����϶�,�������϶��ķ�Χ�������ң�����id�ͣ���������Ӵ��ڴ�С
		   if(options['WindowDraggable']){
		   _this.drag([0,0,wh['w']-options['WindowWidth']-10,wh['h']-options['WindowHeight']-35],id,wh);
		   }
		   //���ô���resize,���������С��Ⱥ͸߶ȣ��´��ڶ���id����������Ӵ��ڴ�С
		   if(options['WindowResizable']){
		   _this.resize(options['WindowMinWidth'],options['WindowMinHeight'],wh['w']-wLeft,wh['h']-wTop-35,id,wh);
		   }
		   //���ı���������ڴ�Сʱ���������϶�����ҷ�����С
		   $(window).wresize(function(){
 						_this.upWinDrag_block($newWin);
						_this.upWinResize_block($newWin);
										  });
		      
							//});
		
		//����ж�����ڣ�������ĳ�����ڣ���ʹ�˴�����ʾ��������
		if(curwinNum){
			var $allwin=$("div.windows");
			$allwin.bind({
						 "mousedown":function(event){  
				                _this.switchZindex($(this));
									},
						 "mouseup":function(){
							 $(this).find('.zzDiv').remove();
							 }		
								});
			}
			
		//������󻯣���С�������ر�
		winClose_btn.click(function(event){
					 event.stopPropagation();
 					 _this.closeWin($(this).parent().parent().parent());
									  });
		//���
		winMaximize_btn.click(function(event){
					 event.stopPropagation();			   
					 if(options['WindowStatus']=="regular"){								 
					 _this.maximizeWin($(this).parent().parent().parent());
					 $(this).hide();
					 winHyimize_btn.show();
					 options['WindowStatus']="maximized";
					 }
 						});
		//��ԭ����
		winHyimize_btn.click(function(event){
					 event.stopPropagation();				  
					 if(options['WindowStatus']=="maximized"){								 
					 _this.hyimizeWin($(this).parent().parent().parent());
					 $(this).hide();
					 winMaximize_btn.show();
					 options['WindowStatus']="regular";
					 }		  
									  });
		//��С������
		winMinimize_btn.click(function(){
						_this.minimize($(this).parent().parent().parent());		   
									   });
		             }else{//����Ѵ��ڴ˴��ڣ��ж��Ƿ�����
					     var wins=$("#"+id),objTab=myLib.desktop.taskBar.findWinTab(wins);
						 if(wins.is(":hidden")){
							  wins.show();
							  objTab.removeClass('defaultTab').addClass('selectTab');//��ֻ��һ������ʱ
						      myLib.desktop.win.switchZindex(wins);
							 }
						
						 }
		},
	upWinResize_block:function(win){
		    
			//���´��ڿɸı��С��Χ,whΪ��������ڴ�С
            var offset=win.offset();
		    win.resizable( "option" ,{'maxWidth':$(window).width()-offset.left-10,'maxHeight':$(window).height()-offset.top-35})
		},
	upWinDrag_block:function(win){
		   var h=win.innerHeight()
		      ,w=win.innerWidth();
			
			//���´��ڿ��϶������С
		    win.draggable( "option", "containment", [10,10,$(window).width()-w-10,$(window).height()-h-35] )
		},	
	drag:function(arr,win_id,wh){
		var _this=this;
		$("#"+win_id)
		.draggable({ 
	    handle: "#"+win_id+' .win_title',
	    iframeFix:false,
	    containment:arr,
		delay: 50 ,
		distance: 30
		})
		.bind("dragstart",function(event,ui){
 					_this.iframFix($(this));	  
						  })
		.bind( "dragstop", function(event, ui) {
			var obj_this=$(this);	
			
			var offset=obj_this.offset();
			//�������ҷ��Χ
			_this.upWinResize_block(obj_this);
			
		    obj_this
			//���´��ڴ洢��λ������
			.data('winLocation',{
			'w':obj_this.width(),
			'h':obj_this.height(),
			'left':offset.left,
			'top':offset.top
			})
			.find('.zzDiv').remove();
         }); 
		
		   $("div.win_title").css("cursor","move");
		},
	resize:function(minW,minH,maxW,maxH,win_id,wh){
		var _this=this;
		$("#"+win_id)
		.resizable({
		minHeight:minH,
		minWidth:minW,
		containment:'document',
		maxWidth:maxW,
		maxHeight:maxH
		})
		.css("position","absolute")
		.bind( "resize", function(event, ui) {
			var h=$(this).innerHeight(),w=$(this).innerWidth(); 	
 			 _this.iframFix($(this));
			 
			//��ҷ�ı䴰�ڴ�С������iframe��Ⱥ͸߶ȣ�����ʾiframe					 
			$(this).children(".winframe").css({"width":w-6,"height":h-29});
				
        })
	   .bind( "resizestop", function(event, ui) {					 
			var obj_this=$(this);
			var offset=obj_this.offset();
			var h=obj_this.innerHeight(),w=obj_this.innerWidth();
			
			//���´��ڿ��϶������С
			_this.upWinDrag_block(obj_this);
			
		    obj_this
			//���´��ڴ洢��λ������
			.data('winLocation',{
			'w':w,
			'h':h,
			'left':offset.left,
			'top':offset.top
			  })
			 //ɾ������iframe�Ĳ�
			.find(".zzDiv").remove();
       });
		}
	}
	
//��߹�����
myLib.NS("desktop.lrBar");
myLib.desktop.lrBar={
	init:function(){
		//��ȡԪ�ض�������
		var myData=myLib.desktop.getMydata();
	    var $default_tools=myData.panel.lrBar['default_tools']
		    ,$def_tools_Btn=$default_tools.find('span')
			,$start_btn=myData.panel.lrBar['start_btn']
			,$start_item=myData.panel.lrBar['start_item']
			,$default_app=myData.panel.lrBar['default_app']
			,$lrBar=myData.panel.lrBar['_this']
			,wh=myData.winWh;
			
		//��ʼ������λ��
		var tops=Math.floor((wh['h']-$lrBar.height())/2)-50;
		$lrBar.css({'top':tops});
		//������ڴ�С�ı䣬����²����λ��
		$(window).wresize(function(){
			var tops=Math.floor(($(window).height()-$lrBar.height())/2)-50;					  
			$lrBar.css({'top':tops});
								   });
		//�������ұ�Ĭ��������򽻻�Ч��	
		$def_tools_Btn.hover(function(){
							$(this).css("background-color","#999");	
								},function(){
									$(this).css("background-color","transparent");
									});	
		//Ĭ��Ӧ�ó�����
		$default_app
		.find('li')
		.hover(function(){
						    $(this).addClass('btnOver');
								 },function(){
									$(this).removeClass('btnOver');		  
										})
		.find('img').dblclick(function(){
						var title=$(this).attr('title'),wid=$(this).parent().attr('id');
						var href= $(this).attr('path');
							myLib.desktop.win.newWin({
													 WindowTitle:title,
													 iframSrc:href,
													 WindowsId:wid,
													 WindowAnimation:'easeInBack'
 													 });			
									})
		.end()
		.end()
		.sortable({
			revert: true
		});
		
		//��ʼ��ť���˵�����Ч��
		$start_btn.click(function(event){
								  event.preventDefault();
								  event.stopPropagation()
								  if($start_item.is(":hidden"))
								  $start_item.show();
								  else
								  $start_item.hide();
								  });
		$("body").click(function(event){
								 event.preventDefault();  
								 $start_item.hide();
									  });
		}
 	}
/*----------------------------------------------------------------------------------	
//�����������ռ䣬���������js����
----------------------------------------------------------------------------------*/
myLib.NS("desktop.taskBar");
myLib.desktop.taskBar={
	timer:function(obj){
		 var curDaytime=new Date().toLocaleString().split(" ");
		 obj.innerHTML=curDaytime[1];
		 obj.title=curDaytime[0];
		 setInterval(function(){obj.innerHTML=new Date().toLocaleString().split(" ")[1];},1000);
		},
	upTaskWidth:function(){
		var myData=myLib.desktop.getMydata()
		    ,$task_bar=myData.panel.taskBar['_this'];
		var maxHdTabNum=Math.floor($(window).width()/100);
		    //�������������
		    $task_bar.width(maxHdTabNum*100);	
			//�洢�������tabĬ������
			$('body').data("maxHdTabNum",maxHdTabNum-2);
		},	
	init:function(){
		//��ȡԪ�ض�������
		var myData=myLib.desktop.getMydata();
 		var $task_lb=myData.panel.taskBar['task_lb']
		    ,$task_bar=myData.panel.taskBar['_this']
			,wh=myData.winWh;
 
		 var _this=this;
		 _this.upTaskWidth();
		 //���ı���������ڴ�Сʱ�����¼������������
		 $(window).wresize(function(){
						_this.upTaskWidth();   
								   });
  		 
 		},
	contextMenu:function(tab,id){
		var _this=this;
		 //��ʼ��������Tab�Ҽ��˵�
		 var data=[
					[{
					text:"���",
					func:function(){
 						$("#myWin_"+tab.data('win')).find('a.winMaximize').trigger('click');
						}
					  },{
					text:"��С��",
					func:function(){
						myLib.desktop.win.minimize($("#myWin_"+tab.data('win')));
						}
						  }]
					,[{
					  text:"�ر�",
					  func:function(){
						  $("#smartMenu_taskTab_menu"+id).remove();
 						  myLib.desktop.win.closeWin($("#myWin_"+tab.data('win')));
						  } 
					  }]
					];
		 myLib.desktop.contextMenu(tab,data,"taskTab_menu"+id,10);
		},
	addWinTab:function(text,id){
		var myData=myLib.desktop.getMydata();
 		var $task_lb=myData.panel.taskBar['task_lb']
		    ,$task_bar=myData.panel.taskBar['_this']
		    ,tid="myWinTab_"+id
			,allTab=$task_lb.find('a')
			,curTabNum=allTab.size()
		    ,docHtml="<a href='#' id='"+tid+"'>"+text+"</a>";
			
			//����µ�tab
		    $task_lb.append($(docHtml));
			var $newTab=$("#"+tid);
			//�Ҽ��˵�
			this.contextMenu($newTab,id);
			
			$task_lb
			.find('a.selectTab')
			.removeClass('selectTab')
			.addClass('defaultTab');
			 
			$newTab
			.data('win',id)
			.addClass('selectTab')
			.click(function(){
					var win=$("#myWin_"+$(this).data('win'));	
					
					if(win.is(":hidden")){
						win.show();
 						$(this).removeClass('defaultTab').addClass('selectTab');//��ֻ��һ������ʱ
						myLib.desktop.win.switchZindex(win);
  						}else{
							if($(this).hasClass('selectTab')){
							myLib.desktop.win.minimize(win);
  							}else{
								myLib.desktop.win.switchZindex(win);
								} 
							  }
 							});
			
			$('body').data("topWinTab",$newTab);
			
			//�������������������ʱ
			if(curTabNum>myData.maxHdTabNum-1){
				var LeftBtn=$('#leftBtn')
				    ,rightBtn=$('#rightBtn')
					,bH;
				
                LeftBtn
				.show()
				.find("a")
				.click(function(){
							        var pos=$task_lb.position();
									if(pos.top<0){
										$task_lb.animate({
                                                  "top":pos.top+40
                                                      }, 50);
										}
									 });
				
				rightBtn
				.show()
				.find("a")
				.click(function(){
									var pos=$task_lb.position(),h=$task_lb.height(),row=h/40;
									if(pos.top>(row-1)*(-40)){
									$task_lb.animate({
                                                  "top": pos.top-40
                                                      }, 50);   
									}
									   });
				
				$task_lb.parent().css("margin","0 100");
				}
	 
		},
	delWinTab:function(wObj){
		var myData=myLib.desktop.getMydata()
 		    ,$task_lb=myData.panel.taskBar['task_lb']
			,$task_bar=myData.panel.taskBar['_this']
			,LeftBtn=$('#leftBtn')
			,rightBtn=$('#rightBtn')
		    ,pos=$task_lb.position();
			
		this.findWinTab(wObj).remove();
		
		var newH=$task_lb.height();
		if(Math.abs(pos.top)==newH){
			LeftBtn.find('a').trigger("click");
			}
		if(newH==40){
			LeftBtn.hide();
			rightBtn.hide();
			$task_lb.parent().css("margin",0);
			}	
		},
	findWinTab:function(wObj){
		var myData=myLib.desktop.getMydata(),
		    $task_lb=myData.panel.taskBar['task_lb'],
		    objTab;
		    $task_lb.find('a').each(function(index){
								var id="#myWin_"+$(this).data("win");		 
								if($(id).is(wObj)){
									objTab=$(this);
									}		 
 										 });
		    return objTab;
		}	
	}
	
//����ͼ��
myLib.NS("desktop.deskIcon");
myLib.desktop.deskIcon={
	//����ͼ������
	arrangeIcons:function(){
		 var myData=myLib.desktop.getMydata()
		    ,winWh=myData.winWh
			,$deskIconBlock=myData.panel.deskIcon['_this']
			,$icon=myData.panel.deskIcon['icon'];
			
		 //��������ͼ������Ԫ�������С
		 $deskIconBlock.css({"width":(winWh['w']-75)+"px","height":(winWh['h']-75)+"px","margin-top":"10px",'margin-left':'75px'});
		 //��ͼ�궨λ
		 var iconNum=$icon.size();
		 //�洢��ǰ�ܹ��ж�������ͼ��
		 $('body').data('deskIconNum',iconNum);
		 var gH=110;//һ��ͼ���ܸ߶ȣ���������margin
		 var gW=120;//ͼ���ܿ��,��������margin
		 var rows=Math.floor((winWh['h']-75)/gH);
		 var cols=Math.ceil(iconNum/rows);
		 var curcol=0,currow=0;
		 //alert(rows);
		 $icon.css({
				   "position":"absolute",
				   "margin":0,
				   "left":function(index,value){
					       var v=curcol*gW+30;
					           if((index+1)%rows==0){
							       curcol=curcol+1;
					              }
						   return v;	 
 						},
					"top":function(index,value){
 							var v=(index-rows*currow)*gH+20;
								if((index+1)%rows==0){
									 currow=currow+1;
									}
						    return v;
							}});
 		return $icon;
		},
	init:function(){
		 //����ǰ���ڿ�Ⱥ͸߶����ݴ洢��bodyԪ����
		 myLib.desktop.winWH();
		 var _this=this;//���ø�������
		 var $icon=_this.arrangeIcons();
		 //������ڴ�С�ı䣬����������ͼ��
		 $(window).wresize(function(){
							myLib.desktop.winWH();//���´��ڴ�С����
							_this.arrangeIcons();
 								   });
		 //ͼ����꾭��Ч��
		 $icon.hover(function(){
						 $(this).addClass("desktop_icon_over");
						 },
						 function(){
							  $(this).removeClass("desktop_icon_over");
							 })
		 //˫��ͼ��򿪴���
		 .dblclick(function(){
							var title=$(this).children("div.text").text(),wid=this.id;
							var href= this.getAttribute("path");
							myLib.desktop.win.newWin({
													 WindowTitle:title,
													 iframSrc:href,
													 WindowsId:wid,
													 WindowAnimation:'easeInBack'
 													 });
							})
		 .draggable({
					revert: true,
					helper: "clone",
					opacity: 0.7,
					start: function(event, ui) {
						var offset=$(this).offset();
						$('body').data("curDragIcon",$(this));
						}
					})
		 .droppable({
                drop: function() {
					var curDragIcon=$('body').data("curDragIcon");
					curDragIcon.insertAfter($(this));
					var l=$(this).css('left'),t=$(this).css('top');
					$(this).css({'left':curDragIcon.css('left'),'top':curDragIcon.css('top')});
					curDragIcon.css({'left':l,'top':t});
					},
           });
		 
		 //��ʼ�������Ҽ��˵�
		 var data=[
					[{
					text:"��ʾ����",
					func:function(){}
						}]
					,[{
					text:"ϵͳ����",
					func:function(){}
					  },{
					text:"��������",
					func:function(){}
						  }]
					,[{
					  text:"�˳�ϵͳ",
					  func:function(){} 
					  }]
					,[{
					  text:"����fleiCms",
					  func:function(){} 
					  }]
					];
		 myLib.desktop.contextMenu($(document.body),data,"body",10);
		}
	}

//��ҳ��������ִ��
$(function(){
		   //�洢���沼��Ԫ�ص�jquery����
		   myLib.desktop.desktopPanel();
		   //��ʼ��������
		   myLib.desktop.taskBar.init();
		   //��ʼ������ͼ��
		   myLib.desktop.deskIcon.init();
		   //��ʼ�������
		   myLib.desktop.lrBar.init();
		   
		   })