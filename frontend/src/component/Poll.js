import React, {Component} from "react";
import {Form} from "react-bootstrap";
import Button from "react-bootstrap/Button";

export default class Poll extends Component {

    constructor(props) {
        super(props);
        this.state = {
            isLoaded: false
        };
        this.getPollById = this.getPollById.bind(this);
        this.showRadioButtons = this.showRadioButtons.bind(this);
    }

    getPollById(pollId) {
        fetch(`${process.env.REACT_APP_API_HOST}/polls/${pollId}`)
            .then(res => res.json())
            .then(data => {
                this.setState({poll: data, isLoaded: true});
            })
            .catch(console.log)
    }

    componentDidMount() {
        this.getPollById(this.props.pollId);
    }

    showRadioButtons() {
        let options = this.state.poll.options;
        let style = {
            fontSize: 16
        }
        let radios = [];
        options.forEach(option => {
            radios.push(
                <div key={option.id} className="form-check" style={style}>
                    <Form.Check type="radio" id={option.id} label={option.value} name="question" value={option.value}
                    onClick={(e) => { this.setState({value: e.target.value, id: e.target.id})}}/>
                </div>)
        });
        return radios;
    }

    handleSubmit = (e) => {
        e.preventDefault();
        const body = JSON.stringify({
            option : { id: this.state.id, value: this.state.value }
        });
        fetch(`${process.env.REACT_APP_API_HOST}/polls/${this.props.pollId}/votes`, {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type':'application/json'
            },
            body: body
        })
            .catch(console.log);
    };

    render() {
        const { isLoaded, poll} = this.state;
        if (!isLoaded || !poll) return null;
        return (
            <Form>
                <Form.Group>
                { this.showRadioButtons()}
                </Form.Group>
                <Button variant="primary" type="submit" onClick={this.handleSubmit}>Submit</Button>
            </Form>
        )
    }
}