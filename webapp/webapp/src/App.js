import React, { Component } from 'react';
import { Container, Button,TextArea ,Image } from 'bloomer'
import 'bulma/css/bulma.css'
import Axios from 'axios';
import Logo from './logo.png'

const buttonStyle={
  display:"flex",
  justifyContent : 'center',
  marginTop : '20px'
}

const padding={
  paddingTop: "40px"
}

class App extends Component {
  constructor(props) {
    super(props);
    this.state = {text:'', showMessage:0}
    this.changeText = this.changeText.bind(this)
    this.submitText = this.submitText.bind(this)
  }

  changeText(e){
    this.setState({text:e.target.value})
  }
  submitText(){
    const regex = /[\n]/gm
    var str = this.state.text
    var a = this.state.showMessage
    str = str.replace(regex, " ")
    this.setState({text:str,showMessage:!a})
    Axios.post('http://192.168.101.18:8080/', {
      content: this.state.text,
    },{responseType:'blob'})
    .then(function (response) {
      
      console.log(response.data)
      const url = window.URL.createObjectURL(new Blob([response.data]))
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', 'output.mp4');
      document.body.appendChild(link);
      link.click();
    })
  }
  render() {
    return (
      <Container>
        <div style={buttonStyle}><Image src={Logo}/></div>
        <div style={buttonStyle}><h2 style={{fontSize:"30px"}}>Generate Videos From Articles</h2></div>
        <form style={padding}>
          <TextArea placeholder="Enter Text" fullWidth={true} multiline={true} rows='15' value={this.state.text} onChange={this.changeText}/>
          <div style={buttonStyle}>
          <Button isColor='info' onClick={this.submitText}>Submit</Button>
          </div>
          {this.state.showMessage ? <div style={buttonStyle}><span>Processing Your Request... In the meantime Grab a Coffee</span></div>: ""}
        </form>
        </Container>
    );
  }
}

export default App;
