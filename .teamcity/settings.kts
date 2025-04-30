import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.sshAgent
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2024.12"

project {

    vcsRoot(HttpsGitJetbrainsTeamTcqaaInternalSampleSshAgentProjectGitRefsHeadsMain)

    buildType(SshAgentBuild)
}

object SshAgentBuild : BuildType({
    name = "ssh-agent-checkoutsssssk"
    paused = true

    vcs {
        checkoutMode = CheckoutMode.ON_AGENT
    }

    steps {
        script {
            id = "simpleRunner"
            executionMode = BuildStep.ExecutionMode.RUN_ON_FAILURE
            scriptContent = """
                git config --global core.sshCommand "ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no"
                git clone ssh://git@git.jetbrains.team/tcqaa-internal/sample-ssh-agent-project.git
                   git config --global --unset core.sshCommand
            """.trimIndent()
            //param("teamcity.kubernetes.executor.pull.policy", "IfNotPresent")
        }
    }

    features {
        sshAgent {
            teamcitySshKey = "ssh-agent-ssh-key"
        }
    }
})

object HttpsGitJetbrainsTeamTcqaaInternalSampleSshAgentProjectGitRefsHeadsMain : GitVcsRoot({
    name = "https://git.jetbrains.team/tcqaa-internal/sample-ssh-agent-project.git#refs/heads/main"
    url = "https://git.jetbrains.team/tcqaa-internal/sample-ssh-agent-project.git"
    branch = "refs/heads/main"
    authMethod = password {
        userName = "Ksenia.Ilina"
        password = "credentialsJSON:686e4db7-894c-4bc4-af77-6b1a8c787738"
    }
})
