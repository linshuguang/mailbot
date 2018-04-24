set -x 

deploy(){
	install_dir=$1
	
	rm -rf $install_dir
	mkdir -p $install_dir
	cp -r * $install_dir
	
	bin_dir=$install_dir/bin
	
	pushd `pwd`

	cd $bin_dir

	rm -rf phantomjs
	unameOut="$(uname -s)"
	case "${unameOut}" in
		Linux*)     
		echo "Linux Installation"
		arch="$(uname -m)"		
		ln -sf phantomjs-2.1.1-linux-$arch/ phantomjs		
		;;
		Darwin*)    
		echo "Mac Installation"
		ln -sf phantomjs-2.1.1-macosx/ phantomjs
		;;
		MINGW*)    
		echo "Windows Installation"
		ln -sf phantomjs-2.1.1-windows/ phantomjs
		;;
		*)         
		echo "unsupport OS"
		exit -1
	esac
    chmod a+x phantomjs/bin/phantomjs*
	popd
	log_path=$install_dir/log
    chmod a+wx $log_path
	
	#sed -i 's/ROOT_PATH/'$install_dir'/g' $install_dir/conf/env.sh
    echo "source \"$install_dir/conf/env.sh\"" > /etc/profile.d/mailbot.sh

	
}

MAILBOT_ROOT_PATH=/usr/local/mailbot
#MAILBOT_ROOT_PATH=`pwd`/deploy/e/project/mailbot/deploy
deploy $MAILBOT_ROOT_PATH



