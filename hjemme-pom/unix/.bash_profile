# funksjoner
function wcgrep {													# fun: wcgrep
    grep -R $1 * | grep -v target | grep -v .git | grep -v "~" | grep -v ".pyc" | grep -v Binary | grep --color -n $1	# fun: wcgrep
}															# fun: wcgrep

# System variables
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0.jdk/Contents/Home	# var: hjemmekatalogen til gjeldende jdk
export M2_HOME=/usr/lib/apache-maven-3.2.1						# var: hjemmeomrade til maven
export PATH=$PATH:$M2_HOME/bin							# var: bin mappa til maven er en del av PATH
export WAR=hjemme.war								# var: miljøvariabel for navn på web archive for hjemme
export WS=$HOME/ws									# var: miljøvariabel for filsti til workspace

# Alias, navigering
alias apps='cd /Applications'							# navigasjon: /Applications
alias cd..='..'									# navigasjon: navigering ned ei mappe og list ut innholdet av denne mappa
alias ..='cd .. && la'								# navigasjon: navigering ned ei mappe og list ut innholdet av denne mappa
alias bus='cd $WS/hjemme/hjemme/hjemme-business'					# navigasjon: hjemme-business
alias cli='cd $WS/hjemme/hjemme/hjemme-client'					# navigasjon: hjemme-client
alias fac='cd $WS/hjemme/hjemme/hjemme-facade'					# navigasjon: hjemme-facade
alias hje='cd $WS/hjemme/hjemme'							# navigasjon: hjemme
alias pom='cd $WS/hjemme/hjemme-pom'							# navigasjon: hjemme-pom
alias tst='cd $WS/hjemme/hjemme/hjemme-test'						# navigasjon: hjemme-test
alias web='cd $WS/hjemme/hjemme-web/'						# navigasjon: hjemme-web
alias wiki='cd $WS/wiki/'								# navigasjon: wiki sider
alias ws='cd $WS && la'								# navigasjon: workspace og list ut innholdet av denne mappa

# Alias, maven
alias mag='mvn archetype:generate'							# cmd: mvn archetype:generate
alias mcc='mvn clean compile'							# cmd: mvn clean compile
alias mci='mvn clean install'							# cmd: mvn clean install
alias mct='mvn clean test'								# cmd: mvn clean tes
alias mcis='mvn clean install -DskipTests'						# cmd: mvn clean install -DskipTests
alias mcp='mvn clean package'							# cmd: mvn clean package
alias mct='mvn clean test'								# cmd: mvn clean test
alias mda='mvn dependency:analyze'							# cmd: mvn dependency:analyze
alias mdt='mvn dependency:tree'							# cmd: mvn dependency:tree
alias mhe='mvn help:effective-pom'							# cmd: mvn help:effective-pom
alias mvd='mvn versions:display-property-updates'					# cmd: mvn versions:display-property-updates

# Alias, diverse:
alias clear='echo ==================== C L E A R ==================== && clear'	# cmd: clear screen
alias cmd='more $HOME/.bash_profile | grep.more'					# cmd: liste alle kommandoer, variabler og funksjoner i .bash_profile
alias grep='grep --color -n'								# cmd: grep med farger og linjenummer
alias grep.more='grep -e "# cmd:" -e "# div:" -e "# fun:" -e "# var:"'		# cmd: grep som filtrerer diverse strenger med som starter med # og slutter med :
alias la='ls -all -G'								# cmd: vis filer og mapper med farger
alias nav='more $HOME/.bash_profile | grep "# navigasjon:"'				# cmd: vis navigasjoner i .bash_profile
alias nav.til='nav | grep'								# cmd: oppgi tekststreng for å se om mulig navigasjon er lagt inn
alias rm.dir='rm -r -f'								# cmd: slett mapper rekursivt
